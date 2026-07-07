package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.domain.AcmeCertificateStatus
import fr.gouv.agora.usecase.acme.repository.AcmeAccountRepository
import fr.gouv.agora.usecase.acme.repository.AcmeCertificateRepository
import fr.gouv.agora.usecase.acme.repository.AcmeChallengeStore
import fr.gouv.agora.usecase.acme.repository.CloudflareCertificateDeployer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.willThrow
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@ExtendWith(MockitoExtension::class)
class AcmeCertificateRenewalUseCaseTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @Mock
    private lateinit var certificateRepository: AcmeCertificateRepository

    @Mock
    private lateinit var accountRepository: AcmeAccountRepository

    @Mock
    private lateinit var challengeStore: AcmeChallengeStore

    @Mock
    private lateinit var cloudflareDeployer: CloudflareCertificateDeployer

    @Mock
    private lateinit var clock: Clock

    @InjectMocks
    private lateinit var useCase: AcmeCertificateRenewalUseCase

    companion object {
        private val NOW = LocalDateTime.of(2026, 6, 25, 3, 0, 0)
        private val FIXED_CLOCK = Clock.fixed(
            NOW.toInstant(ZoneOffset.UTC),
            ZoneId.of("UTC"),
        )
    }

    @Nested
    inner class RenewIfNeeded {

        @Test
        fun `renewIfNeeded - when ACME_ENABLED is false - should do nothing and not call any dependency`() {
            // Given
            given(acmeConfig.enabled).willReturn(false)

            // When
            useCase.renewIfNeeded()

            // Then
            then(certificateRepository).shouldHaveNoInteractions()
            then(accountRepository).shouldHaveNoInteractions()
            then(challengeStore).shouldHaveNoInteractions()
            then(cloudflareDeployer).shouldHaveNoInteractions()
        }

        @Test
        fun `renewIfNeeded - when certificate is valid and status is DEPLOYED - should do nothing`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            val existingCert = AcmeCertificate(
                domain = "agora.gouv.fr",
                certificatePem = "cert-pem",
                privateKeyPem = "key-pem",
                expiresAt = NOW.plusDays(60),
                status = AcmeCertificateStatus.DEPLOYED,
            )
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(existingCert)

            // When
            useCase.renewIfNeeded()

            // Then — aucune action au-delà du chargement du certificat
            then(accountRepository).shouldHaveNoInteractions()
            then(challengeStore).shouldHaveNoInteractions()
            then(cloudflareDeployer).shouldHaveNoInteractions()
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(certificateRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `renewIfNeeded - when certificate is valid but status is TO_DEPLOY - should skip ACME provisioning and deploy to Cloudflare directly`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            val existingCert = AcmeCertificate(
                domain = "agora.gouv.fr",
                certificatePem = "cert-pem",
                privateKeyPem = "key-pem",
                expiresAt = NOW.plusDays(60),
                status = AcmeCertificateStatus.TO_DEPLOY,
            )
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(existingCert)

            // When
            useCase.renewIfNeeded()

            // Then — Cloudflare est appelé avec le certificat existant, sans provisioning ACME
            then(accountRepository).shouldHaveNoInteractions()
            then(challengeStore).shouldHaveNoInteractions()
            then(cloudflareDeployer).should().deployCertificate("cert-pem", "key-pem")
            then(certificateRepository).should().markAsDeployed("agora.gouv.fr")
        }

        @Test
        fun `renewIfNeeded - when certificate is valid and TO_DEPLOY and Cloudflare deployment succeeds - should mark certificate as deployed`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            val existingCert = AcmeCertificate(
                domain = "agora.gouv.fr",
                certificatePem = "cert-pem",
                privateKeyPem = "key-pem",
                expiresAt = NOW.plusDays(60),
                status = AcmeCertificateStatus.TO_DEPLOY,
            )
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(existingCert)

            // When
            useCase.renewIfNeeded()

            // Then
            then(certificateRepository).should().markAsDeployed("agora.gouv.fr")
        }

        @Test
        fun `renewIfNeeded - when certificate is valid and TO_DEPLOY and Cloudflare deployment fails - should NOT mark certificate as deployed`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            val existingCert = AcmeCertificate(
                domain = "agora.gouv.fr",
                certificatePem = "cert-pem",
                privateKeyPem = "key-pem",
                expiresAt = NOW.plusDays(60),
                status = AcmeCertificateStatus.TO_DEPLOY,
            )
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(existingCert)
            willThrow(RuntimeException("Cloudflare error")).given(cloudflareDeployer)
                .deployCertificate("cert-pem", "key-pem")

            // When
            val thrown = runCatching { useCase.renewIfNeeded() }

            // Then — l'exception est propagée et markAsDeployed n'est jamais appelé
            assertThat(thrown.isFailure).isTrue()
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(certificateRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `renewIfNeeded - when certificate expires in less than 30 days - should proceed to renewal`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.serverUrl).willReturn("https://acme.sectigo.com/v2/DV")
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            val existingCert = AcmeCertificate(
                domain = "agora.gouv.fr",
                certificatePem = "cert-pem",
                privateKeyPem = "key-pem",
                expiresAt = NOW.plusDays(10),
                status = AcmeCertificateStatus.DEPLOYED,
            )
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(existingCert)
            // accountRepository retourne null → le use case essaiera de créer un compte ACME
            // On ne peut pas aller plus loin sans un vrai serveur ACME, mais on vérifie que
            // le flot a bien dépassé le guard clause d'expiration
            given(accountRepository.loadAccount("https://acme.sectigo.com/v2/DV")).willReturn(null)

            // When / Then — l'exception vient du fait qu'il n'y a pas de vrai serveur ACME,
            // ce qui confirme que la logique de renouvellement a bien été déclenchée
            val thrown = runCatching { useCase.renewIfNeeded() }
            assertThat(thrown.isFailure).isTrue()

            // Le certificateRepository a bien été interrogé
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(accountRepository).should().loadAccount("https://acme.sectigo.com/v2/DV")
        }

        @Test
        fun `renewIfNeeded - when no certificate exists - should proceed to issuance`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.serverUrl).willReturn("https://acme.sectigo.com/v2/DV")
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(null)
            given(accountRepository.loadAccount("https://acme.sectigo.com/v2/DV")).willReturn(null)

            // When / Then
            val thrown = runCatching { useCase.renewIfNeeded() }
            assertThat(thrown.isFailure).isTrue()

            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(accountRepository).should().loadAccount("https://acme.sectigo.com/v2/DV")
        }
    }
}

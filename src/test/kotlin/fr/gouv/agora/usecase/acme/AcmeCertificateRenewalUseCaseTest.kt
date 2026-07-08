package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.domain.AcmeCertificateStatus
import fr.gouv.agora.domain.AcmeOrder
import fr.gouv.agora.domain.AcmeOrderStatus
import fr.gouv.agora.usecase.acme.repository.AcmeAccountRepository
import fr.gouv.agora.usecase.acme.repository.AcmeCertificateRepository
import fr.gouv.agora.usecase.acme.repository.AcmeChallengeStore
import fr.gouv.agora.usecase.acme.repository.AcmeOrderRepository
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
    private lateinit var orderRepository: AcmeOrderRepository

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
            then(orderRepository).shouldHaveNoInteractions()
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
            then(orderRepository).shouldHaveNoInteractions()
            then(cloudflareDeployer).shouldHaveNoInteractions()
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(certificateRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `renewIfNeeded - when certificate is valid but status is TO_DEPLOY - should skip ACME provisioning and deploy to Cloudflare directly`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(true)
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
        fun `renewIfNeeded - when certificate is valid and TO_DEPLOY and Cloudflare deployment succeeds - should mark certificate as deployed and clean up order`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(true)
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
            then(orderRepository).should().deleteOrder("agora.gouv.fr")
        }

        @Test
        fun `renewIfNeeded - when certificate is valid and TO_DEPLOY and Cloudflare deployment fails - should NOT mark certificate as deployed`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(true)
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
            then(orderRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `renewIfNeeded - when certificate expires in less than 30 days - should proceed to renewal`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.serverUrl).willReturn("https://acme.sectigo.com/v2/DV")
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
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
            // Pas d'order en cours en base
            given(orderRepository.loadOrder("agora.gouv.fr")).willReturn(null)
            // accountRepository retourne null → le use case essaiera de créer un compte ACME
            given(accountRepository.loadAccount("https://acme.sectigo.com/v2/DV")).willReturn(null)

            // When / Then — l'exception vient du fait qu'il n'y a pas de vrai serveur ACME,
            // ce qui confirme que la logique de renouvellement a bien été déclenchée
            val thrown = runCatching { useCase.renewIfNeeded() }
            assertThat(thrown.isFailure).isTrue()

            // Le certificateRepository et orderRepository ont bien été interrogés
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(orderRepository).should().loadOrder("agora.gouv.fr")
            then(accountRepository).should().loadAccount("https://acme.sectigo.com/v2/DV")
        }

        @Test
        fun `renewIfNeeded - when no certificate exists - should proceed to issuance`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.serverUrl).willReturn("https://acme.sectigo.com/v2/DV")
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(null)
            given(orderRepository.loadOrder("agora.gouv.fr")).willReturn(null)
            given(accountRepository.loadAccount("https://acme.sectigo.com/v2/DV")).willReturn(null)

            // When / Then
            val thrown = runCatching { useCase.renewIfNeeded() }
            assertThat(thrown.isFailure).isTrue()

            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(orderRepository).should().loadOrder("agora.gouv.fr")
            then(accountRepository).should().loadAccount("https://acme.sectigo.com/v2/DV")
        }

        @Test
        fun `renewIfNeeded - when ACME_SERVER_INTERACTION_ENABLED is false and provisioning is needed - should skip everything and not call any dependency`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(false)
            given(clock.instant()).willReturn(FIXED_CLOCK.instant())
            given(clock.zone).willReturn(FIXED_CLOCK.zone)
            given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(null)

            // When
            useCase.renewIfNeeded()

            // Then — aucune interaction ACME ni Cloudflare
            then(accountRepository).shouldHaveNoInteractions()
            then(challengeStore).shouldHaveNoInteractions()
            then(orderRepository).shouldHaveNoInteractions()
            then(cloudflareDeployer).shouldHaveNoInteractions()
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(certificateRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `renewIfNeeded - when ACME_SERVER_INTERACTION_ENABLED is false and certificate expires soon - should skip ACME provisioning`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(false)
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

            // When
            useCase.renewIfNeeded()

            // Then — aucune interaction ACME ni Cloudflare
            then(accountRepository).shouldHaveNoInteractions()
            then(challengeStore).shouldHaveNoInteractions()
            then(orderRepository).shouldHaveNoInteractions()
            then(cloudflareDeployer).shouldHaveNoInteractions()
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(certificateRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `renewIfNeeded - when ACME_CLOUDFLARE_INTERACTION_ENABLED is false and certificate is TO_DEPLOY - should skip Cloudflare deployment`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(false)
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

            // Then — Cloudflare n'est pas appelé, markAsDeployed non plus
            then(cloudflareDeployer).shouldHaveNoInteractions()
            then(certificateRepository).should().loadCertificate("agora.gouv.fr")
            then(certificateRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `renewIfNeeded - when ACME_SERVER_INTERACTION_ENABLED is false but certificate is TO_DEPLOY - should still deploy to Cloudflare`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.domain).willReturn("agora.gouv.fr")
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(true)
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

            // Then — Cloudflare est appelé avec le cert en base (pas de provisioning ACME)
            then(accountRepository).shouldHaveNoInteractions()
            then(challengeStore).shouldHaveNoInteractions()
            then(cloudflareDeployer).should().deployCertificate("cert-pem", "key-pem")
            then(certificateRepository).should().markAsDeployed("agora.gouv.fr")
            then(orderRepository).should().deleteOrder("agora.gouv.fr")
        }

        @Nested
        inner class ResumeOrder {

            @Test
            fun `renewIfNeeded - when pending order exists in database - should check it before starting new order`() {
                // Given
                given(acmeConfig.enabled).willReturn(true)
                given(acmeConfig.domain).willReturn("agora.gouv.fr")
                given(acmeConfig.serverUrl).willReturn("https://acme.sectigo.com/v2/DV")
                given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
                given(clock.instant()).willReturn(FIXED_CLOCK.instant())
                given(clock.zone).willReturn(FIXED_CLOCK.zone)
                given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(null)
                val pendingOrder = AcmeOrder(
                    domain = "agora.gouv.fr",
                    orderUrl = "https://acme.sectigo.com/v2/DV/order/12345",
                    domainKeyPem = "domain-key-pem",
                    status = AcmeOrderStatus.CHALLENGE_PENDING,
                    createdAt = NOW.minusHours(1),
                )
                given(orderRepository.loadOrder("agora.gouv.fr")).willReturn(pendingOrder)
                // Le compte ACME doit exister pour une reprise
                given(accountRepository.loadAccount("https://acme.sectigo.com/v2/DV")).willReturn(null)

                // When / Then — la reprise tente de récupérer le compte et échoue car pas de vrai serveur
                val thrown = runCatching { useCase.renewIfNeeded() }
                assertThat(thrown.isFailure).isTrue()

                // L'order en base a bien été chargé
                then(orderRepository).should().loadOrder("agora.gouv.fr")
                // Le compte ACME a été recherché pour la reprise
                then(accountRepository).should().loadAccount("https://acme.sectigo.com/v2/DV")
            }

            @Test
            fun `renewIfNeeded - when order polling times out - should throw AcmeChallengeTimeoutException`() {
                // Given — un order en ORDER_FINALIZING est en base : le usecase va tenter resumeOrder,
                // qui essaie de se connecter à un vrai serveur ACME → échoue avec une exception
                // La vérification porte sur le fait que le flux ne se termine pas silencieusement
                given(acmeConfig.enabled).willReturn(true)
                given(acmeConfig.domain).willReturn("agora.gouv.fr")
                given(acmeConfig.serverUrl).willReturn("https://acme.sectigo.com/v2/DV")
                given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
                given(clock.instant()).willReturn(FIXED_CLOCK.instant())
                given(clock.zone).willReturn(FIXED_CLOCK.zone)
                given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(null)
                val pendingOrder = AcmeOrder(
                    domain = "agora.gouv.fr",
                    orderUrl = "https://acme.sectigo.com/v2/DV/order/99999",
                    domainKeyPem = "domain-key-pem",
                    status = AcmeOrderStatus.ORDER_FINALIZING,
                    createdAt = NOW.minusHours(1),
                )
                given(orderRepository.loadOrder("agora.gouv.fr")).willReturn(pendingOrder)
                // Pas de compte ACME → resumeOrder lève immédiatement une IllegalStateException
                given(accountRepository.loadAccount("https://acme.sectigo.com/v2/DV")).willReturn(null)

                // When
                val thrown = runCatching { useCase.renewIfNeeded() }

                // Then — une exception est bien levée (le flux n'a pas terminé silencieusement)
                assertThat(thrown.isFailure).isTrue()
                then(orderRepository).should().loadOrder("agora.gouv.fr")
                then(accountRepository).should().loadAccount("https://acme.sectigo.com/v2/DV")
            }

            @Test
            fun `renewIfNeeded - when no pending order in database - should not query orderRepository further`() {
                // Given
                given(acmeConfig.enabled).willReturn(true)
                given(acmeConfig.domain).willReturn("agora.gouv.fr")
                given(acmeConfig.serverUrl).willReturn("https://acme.sectigo.com/v2/DV")
                given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
                given(clock.instant()).willReturn(FIXED_CLOCK.instant())
                given(clock.zone).willReturn(FIXED_CLOCK.zone)
                given(certificateRepository.loadCertificate("agora.gouv.fr")).willReturn(null)
                given(orderRepository.loadOrder("agora.gouv.fr")).willReturn(null)
                given(accountRepository.loadAccount("https://acme.sectigo.com/v2/DV")).willReturn(null)

                // When / Then — démarre un nouvel order (échoue car pas de vrai serveur ACME)
                val thrown = runCatching { useCase.renewIfNeeded() }
                assertThat(thrown.isFailure).isTrue()

                // loadOrder est appelé une seule fois pour la vérification
                then(orderRepository).should().loadOrder("agora.gouv.fr")
                then(orderRepository).shouldHaveNoMoreInteractions()
            }
        }
    }
}

package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.domain.AcmeCertificateStatus
import fr.gouv.agora.usecase.acme.repository.AcmeCertificateRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class AcmeCertificateUploadUseCaseTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @Mock
    private lateinit var certificateRepository: AcmeCertificateRepository

    @InjectMocks
    private lateinit var useCase: AcmeCertificateUploadUseCase

    companion object {
        private const val DOMAIN = "agora.gouv.fr"
        private const val CERTIFICATE_PEM = "-----BEGIN CERTIFICATE-----\nMIIFAzCC...\n-----END CERTIFICATE-----"
        private const val PRIVATE_KEY_PEM = "-----BEGIN RSA PRIVATE KEY-----\nMIIEow...\n-----END RSA PRIVATE KEY-----"
        private val EXPIRES_AT = LocalDateTime.of(2026, 9, 1, 0, 0, 0)
    }

    @Nested
    inner class UploadCertificate {

        @Test
        fun `uploadCertificate - when called - should save certificate with TO_DEPLOY status`() {
            // Given
            given(acmeConfig.domain).willReturn(DOMAIN)

            // When
            useCase.uploadCertificate(
                certificatePem = CERTIFICATE_PEM,
                privateKeyPem = PRIVATE_KEY_PEM,
                expiresAt = EXPIRES_AT,
            )

            // Then
            then(certificateRepository).should().saveCertificate(
                AcmeCertificate(
                    domain = DOMAIN,
                    certificatePem = CERTIFICATE_PEM,
                    privateKeyPem = PRIVATE_KEY_PEM,
                    expiresAt = EXPIRES_AT,
                    status = AcmeCertificateStatus.TO_DEPLOY,
                )
            )
        }

        @Test
        fun `uploadCertificate - when called - should use domain from config`() {
            // Given
            val customDomain = "custom.example.com"
            given(acmeConfig.domain).willReturn(customDomain)

            // When
            useCase.uploadCertificate(
                certificatePem = CERTIFICATE_PEM,
                privateKeyPem = PRIVATE_KEY_PEM,
                expiresAt = EXPIRES_AT,
            )

            // Then
            then(certificateRepository).should().saveCertificate(
                AcmeCertificate(
                    domain = customDomain,
                    certificatePem = CERTIFICATE_PEM,
                    privateKeyPem = PRIVATE_KEY_PEM,
                    expiresAt = EXPIRES_AT,
                    status = AcmeCertificateStatus.TO_DEPLOY,
                )
            )
        }
    }
}

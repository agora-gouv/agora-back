package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.domain.AcmeCertificateStatus
import fr.gouv.agora.infrastructure.acme.repository.AcmeCertificateDAO
import fr.gouv.agora.infrastructure.acme.repository.AcmeCertificateJpaRepository
import fr.gouv.agora.infrastructure.acme.repository.AcmeCertificateRepositoryImpl
import fr.gouv.agora.infrastructure.acme.repository.AcmeCryptoHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AcmeCertificateRepositoryImplTest {

    @Mock
    private lateinit var jpaRepository: AcmeCertificateJpaRepository

    @Mock
    private lateinit var cryptoHelper: AcmeCryptoHelper

    @InjectMocks
    private lateinit var repository: AcmeCertificateRepositoryImpl

    @Captor
    private lateinit var daoCaptor: ArgumentCaptor<AcmeCertificateDAO>

    private fun aDao(privateKey: String = "encrypted-key", status: AcmeCertificateStatus = AcmeCertificateStatus.TO_DEPLOY) =
        AcmeCertificateDAO(
            id = UUID.randomUUID(),
            domain = "example.com",
            certificate = "-----BEGIN CERTIFICATE-----\ncert-content\n-----END CERTIFICATE-----",
            privateKey = privateKey,
            expiresAt = LocalDateTime.of(2027, 1, 1, 0, 0),
            createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            status = status,
        )

    @Nested
    inner class LoadCertificate {

        @Test
        fun `loadCertificate - when certificate exists - should decrypt privateKey and return AcmeCertificate`() {
            // Given
            val dao = aDao(privateKey = "encrypted-key", status = AcmeCertificateStatus.DEPLOYED)
            given(jpaRepository.findFirstByDomainOrderByCreatedAtDesc("example.com")).willReturn(dao)
            given(cryptoHelper.decrypt("encrypted-key")).willReturn("plaintext-key")

            // When
            val result = repository.loadCertificate("example.com")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.privateKeyPem).isEqualTo("plaintext-key")
            assertThat(result.domain).isEqualTo("example.com")
            assertThat(result.certificatePem).isEqualTo(dao.certificate)
            assertThat(result.status).isEqualTo(AcmeCertificateStatus.DEPLOYED)
        }

        @Test
        fun `loadCertificate - when no certificate in database - should return null`() {
            // Given
            given(jpaRepository.findFirstByDomainOrderByCreatedAtDesc("example.com")).willReturn(null)

            // When
            val result = repository.loadCertificate("example.com")

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class SaveCertificate {

        @Test
        fun `saveCertificate - when saving - should encrypt privateKeyPem and use certificate status`() {
            // Given
            given(cryptoHelper.encrypt("plaintext-key")).willReturn("encrypted-key")
            val certificate = AcmeCertificate(
                domain = "example.com",
                certificatePem = "-----BEGIN CERTIFICATE-----\ncert\n-----END CERTIFICATE-----",
                privateKeyPem = "plaintext-key",
                expiresAt = LocalDateTime.of(2027, 1, 1, 0, 0),
                status = AcmeCertificateStatus.TO_DEPLOY,
            )

            // When
            repository.saveCertificate(certificate)

            // Then
            then(jpaRepository).should().save(daoCaptor.capture())
            val savedDao = daoCaptor.value
            assertThat(savedDao.privateKey).isEqualTo("encrypted-key")
            assertThat(savedDao.domain).isEqualTo("example.com")
            assertThat(savedDao.status).isEqualTo(AcmeCertificateStatus.TO_DEPLOY)
        }
    }

    @Nested
    inner class MarkAsDeployed {

        @Test
        fun `markAsDeployed - when called - should delegate to jpaRepository with DEPLOYED status and deployedAt timestamp`() {
            // Given
            val deployedAt = LocalDateTime.of(2026, 6, 25, 3, 0, 0)

            // When
            repository.markAsDeployed("example.com", deployedAt)

            // Then
            then(jpaRepository).should().markAsDeployedForLatestByDomain("example.com", AcmeCertificateStatus.DEPLOYED, deployedAt)
        }
    }
}

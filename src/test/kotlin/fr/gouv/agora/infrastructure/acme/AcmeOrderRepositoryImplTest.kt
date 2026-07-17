package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.domain.AcmeOrder
import fr.gouv.agora.domain.AcmeOrderStatus
import fr.gouv.agora.infrastructure.acme.repository.AcmeCryptoHelper
import fr.gouv.agora.infrastructure.acme.repository.AcmeOrderDAO
import fr.gouv.agora.infrastructure.acme.repository.AcmeOrderJpaRepository
import fr.gouv.agora.infrastructure.acme.repository.AcmeOrderRepositoryImpl
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
class AcmeOrderRepositoryImplTest {

    @Mock
    private lateinit var jpaRepository: AcmeOrderJpaRepository

    @Mock
    private lateinit var cryptoHelper: AcmeCryptoHelper

    @InjectMocks
    private lateinit var repository: AcmeOrderRepositoryImpl

    @Captor
    private lateinit var daoCaptor: ArgumentCaptor<AcmeOrderDAO>

    private fun aDao(domainKeyPem: String = "encrypted-key") = AcmeOrderDAO(
        id = UUID.randomUUID(),
        domain = "example.com",
        orderUrl = "https://acme.example.com/order/1",
        domainKeyPem = domainKeyPem,
        status = AcmeOrderStatus.CHALLENGE_PENDING,
        createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
    )

    @Nested
    inner class LoadOrder {

        @Test
        fun `loadOrder - when order exists in database - should decrypt domainKeyPem and return AcmeOrder`() {
            // Given
            val dao = aDao(domainKeyPem = "encrypted-key")
            given(jpaRepository.findFirstByDomainOrderByCreatedAtDesc("example.com")).willReturn(dao)
            given(cryptoHelper.decrypt("encrypted-key")).willReturn("plaintext-key")

            // When
            val result = repository.loadOrder("example.com")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.domain).isEqualTo("example.com")
            assertThat(result.domainKeyPem).isEqualTo("plaintext-key")
            assertThat(result.orderUrl).isEqualTo(dao.orderUrl)
            assertThat(result.status).isEqualTo(AcmeOrderStatus.CHALLENGE_PENDING)
        }

        @Test
        fun `loadOrder - when no order in database - should return null without calling cryptoHelper`() {
            // Given
            given(jpaRepository.findFirstByDomainOrderByCreatedAtDesc("example.com")).willReturn(null)

            // When
            val result = repository.loadOrder("example.com")

            // Then
            assertThat(result).isNull()
            then(cryptoHelper).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class SaveOrder {

        @Test
        fun `saveOrder - when saving an order - should encrypt domainKeyPem before persisting`() {
            // Given
            given(cryptoHelper.encrypt("plaintext-key")).willReturn("encrypted-key")
            val order = AcmeOrder(
                domain = "example.com",
                orderUrl = "https://acme.example.com/order/1",
                domainKeyPem = "plaintext-key",
                status = AcmeOrderStatus.CHALLENGE_PENDING,
                createdAt = LocalDateTime.of(2026, 1, 1, 0, 0),
            )

            // When
            repository.saveOrder(order)

            // Then
            then(jpaRepository).should().save(daoCaptor.capture())
            val savedDao = daoCaptor.value
            assertThat(savedDao.domainKeyPem).isEqualTo("encrypted-key")
            assertThat(savedDao.domain).isEqualTo("example.com")
            assertThat(savedDao.status).isEqualTo(AcmeOrderStatus.CHALLENGE_PENDING)
        }
    }

    @Nested
    inner class UpdateOrderStatus {

        @Test
        fun `updateOrderStatus - when called - should delegate to jpaRepository`() {
            // When
            repository.updateOrderStatus("example.com", AcmeOrderStatus.ORDER_FINALIZING)

            // Then
            then(jpaRepository).should().updateStatusForLatestByDomain("example.com", AcmeOrderStatus.ORDER_FINALIZING)
        }
    }

    @Nested
    inner class DeleteOrder {

        @Test
        fun `deleteOrder - when called - should delegate to jpaRepository`() {
            // When
            repository.deleteOrder("example.com")

            // Then
            then(jpaRepository).should().deleteByDomain("example.com")
        }
    }
}

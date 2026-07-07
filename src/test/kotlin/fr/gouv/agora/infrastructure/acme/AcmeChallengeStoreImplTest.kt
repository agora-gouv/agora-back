package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.infrastructure.acme.repository.AcmeChallengeDAO
import fr.gouv.agora.infrastructure.acme.repository.AcmeChallengeJpaRepository
import fr.gouv.agora.infrastructure.acme.repository.AcmeChallengeStoreImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class AcmeChallengeStoreImplTest {

    @Mock
    private lateinit var jpaRepository: AcmeChallengeJpaRepository

    @InjectMocks
    private lateinit var store: AcmeChallengeStoreImpl

    @Nested
    inner class StoreChallenge {

        @Test
        fun `storeChallenge - when called - should save dao to repository`() {
            // Given
            val token = "abc123token"
            val keyAuth = "abc123token.thumbprint"
            val captor = ArgumentCaptor.forClass(AcmeChallengeDAO::class.java)

            // When
            store.storeChallenge(token, keyAuth)

            // Then
            then(jpaRepository).should().save(captor.capture())
            assertThat(captor.value.token).isEqualTo(token)
            assertThat(captor.value.keyAuthorization).isEqualTo(keyAuth)
        }
    }

    @Nested
    inner class GetChallenge {

        @Test
        fun `getChallenge - when token exists - should return keyAuthorization`() {
            // Given
            val token = "abc123token"
            val keyAuth = "abc123token.thumbprint"
            val dao = AcmeChallengeDAO(token = token, keyAuthorization = keyAuth, createdAt = LocalDateTime.now())
            given(jpaRepository.findById(token)).willReturn(Optional.of(dao))

            // When
            val result = store.getChallenge(token)

            // Then
            assertThat(result).isEqualTo(keyAuth)
        }

        @Test
        fun `getChallenge - when token does not exist - should return null`() {
            // Given
            val token = "unknown-token"
            given(jpaRepository.findById(token)).willReturn(Optional.empty())

            // When
            val result = store.getChallenge(token)

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class ClearChallenge {

        @Test
        fun `clearChallenge - when called - should delete token from repository`() {
            // Given
            val token = "abc123token"

            // When
            store.clearChallenge(token)

            // Then
            then(jpaRepository).should().deleteById(token)
        }

        @Test
        fun `clearChallenge - when token does not exist - should still call deleteById without throwing`() {
            // Given
            val token = "non-existent-token"

            // When / Then (no exception expected)
            store.clearChallenge(token)

            then(jpaRepository).should().deleteById(token)
        }
    }
}

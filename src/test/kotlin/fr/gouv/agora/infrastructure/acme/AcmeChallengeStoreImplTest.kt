package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.infrastructure.acme.repository.AcmeChallengeStoreImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AcmeChallengeStoreImplTest {

    private lateinit var store: AcmeChallengeStoreImpl

    @BeforeEach
    fun setUp() {
        store = AcmeChallengeStoreImpl()
    }

    @Nested
    inner class StoreAndGet {

        @Test
        fun `storeChallenge - when token is stored - should be retrievable via getChallenge`() {
            // Given
            val token = "abc123token"
            val keyAuth = "abc123token.thumbprint"

            // When
            store.storeChallenge(token, keyAuth)

            // Then
            assertThat(store.getChallenge(token)).isEqualTo(keyAuth)
        }

        @Test
        fun `getChallenge - when token does not exist - should return null`() {
            // Given
            val token = "unknown-token"

            // When
            val result = store.getChallenge(token)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `storeChallenge - when called twice with same token - should overwrite previous value`() {
            // Given
            val token = "abc123token"
            store.storeChallenge(token, "old-key-auth")

            // When
            store.storeChallenge(token, "new-key-auth")

            // Then
            assertThat(store.getChallenge(token)).isEqualTo("new-key-auth")
        }
    }

    @Nested
    inner class Clear {

        @Test
        fun `clearChallenge - when called - should remove token so getChallenge returns null`() {
            // Given
            val token = "abc123token"
            store.storeChallenge(token, "abc123token.thumbprint")

            // When
            store.clearChallenge(token)

            // Then
            assertThat(store.getChallenge(token)).isNull()
        }

        @Test
        fun `clearChallenge - when token does not exist - should not throw`() {
            // Given
            val token = "non-existent-token"

            // When / Then (no exception expected)
            store.clearChallenge(token)
        }
    }
}

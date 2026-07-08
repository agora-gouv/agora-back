package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.infrastructure.acme.repository.AcmeCryptoHelper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Base64

@ExtendWith(MockitoExtension::class)
class AcmeCryptoHelperTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @InjectMocks
    private lateinit var cryptoHelper: AcmeCryptoHelper

    companion object {
        /** Clé AES-256 valide : 32 bytes encodés en base64. */
        private val VALID_KEY_32_BYTES: String =
            Base64.getEncoder().encodeToString(ByteArray(32) { it.toByte() })

        /** Clé trop courte : 16 bytes encodés en base64 (AES-128, rejeté car != 32 bytes). */
        private val SHORT_KEY_16_BYTES: String =
            Base64.getEncoder().encodeToString(ByteArray(16) { it.toByte() })
    }

    @Nested
    inner class EncryptDecrypt {

        @Test
        fun `encrypt then decrypt - when key is valid - should return original plaintext`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn(VALID_KEY_32_BYTES)
            val plainText = "my-super-secret-private-key-pem"

            // When
            val encrypted = cryptoHelper.encrypt(plainText)
            val decrypted = cryptoHelper.decrypt(encrypted)

            // Then
            assertThat(decrypted).isEqualTo(plainText)
        }

        @Test
        fun `encrypt - when called twice with same plaintext - should return different ciphertexts (random IV)`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn(VALID_KEY_32_BYTES)
            val plainText = "same-input-text"

            // When
            val encrypted1 = cryptoHelper.encrypt(plainText)
            val encrypted2 = cryptoHelper.encrypt(plainText)

            // Then — chaque chiffrement produit un IV différent, donc des ciphertexts différents
            assertThat(encrypted1).isNotEqualTo(encrypted2)
        }
    }

    @Nested
    inner class InvalidKey {

        @Test
        fun `encrypt - when encryptionKey is blank - should throw IllegalStateException`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn("")

            // When / Then
            assertThatThrownBy { cryptoHelper.encrypt("anything") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("ACME_ENCRYPTION_KEY is not configured")
        }

        @Test
        fun `encrypt - when encryptionKey is not valid base64 - should throw IllegalStateException`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn("this-is-not-valid-base64!!!")

            // When / Then
            assertThatThrownBy { cryptoHelper.encrypt("anything") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("not a valid base64")
        }

        @Test
        fun `encrypt - when encryptionKey decodes to less than 32 bytes - should throw IllegalStateException`() {
            // Given — clé valide en base64 mais seulement 16 bytes décodés
            given(acmeConfig.encryptionKey).willReturn(SHORT_KEY_16_BYTES)

            // When / Then
            assertThatThrownBy { cryptoHelper.encrypt("anything") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("must be exactly 32 bytes")
        }
    }
}

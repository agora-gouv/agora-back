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
        // Clé AES-256 valide de test (32 bytes encodés en base64)
        private val VALID_KEY_BASE64 = Base64.getEncoder().encodeToString(ByteArray(32) { it.toByte() })
    }

    @Nested
    inner class EncryptAndDecrypt {

        @Test
        fun `encrypt then decrypt - should return original plaintext`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn(VALID_KEY_BASE64)
            val plaintext = "-----BEGIN RSA PRIVATE KEY-----\nMIIEowIBAAKCAQEA...\n-----END RSA PRIVATE KEY-----"

            // When
            val encrypted = cryptoHelper.encrypt(plaintext)
            val decrypted = cryptoHelper.decrypt(encrypted)

            // Then
            assertThat(decrypted).isEqualTo(plaintext)
        }

        @Test
        fun `encrypt - two calls with same input - should produce different ciphertexts (random IV)`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn(VALID_KEY_BASE64)
            val plaintext = "my-secret-private-key"

            // When
            val encrypted1 = cryptoHelper.encrypt(plaintext)
            val encrypted2 = cryptoHelper.encrypt(plaintext)

            // Then
            assertThat(encrypted1).isNotEqualTo(encrypted2)
        }

        @Test
        fun `decrypt - when ciphertext is tampered - should throw exception`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn(VALID_KEY_BASE64)
            val plaintext = "my-secret"
            val encrypted = cryptoHelper.encrypt(plaintext)
            // Tamper: flip a byte by appending garbage
            val tampered = encrypted.dropLast(4) + "XXXX"

            // When / Then
            assertThatThrownBy { cryptoHelper.decrypt(tampered) }
                .isInstanceOf(Exception::class.java)
        }
    }

    @Nested
    inner class KeyValidation {

        @Test
        fun `resolveKey - when encryptionKey is blank - should throw IllegalStateException`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn("")

            // When / Then
            assertThatThrownBy { cryptoHelper.encrypt("anything") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("ACME_ENCRYPTION_KEY")
        }

        @Test
        fun `resolveKey - when encryptionKey is not valid base64 - should throw IllegalStateException`() {
            // Given
            given(acmeConfig.encryptionKey).willReturn("not-valid-base64!!!")

            // When / Then
            assertThatThrownBy { cryptoHelper.encrypt("anything") }
                .isInstanceOf(IllegalStateException::class.java)
        }

        @Test
        fun `resolveKey - when encryptionKey decodes to wrong length - should throw IllegalStateException`() {
            // Given — clé de 16 bytes (AES-128), pas 32
            val shortKey = Base64.getEncoder().encodeToString(ByteArray(16))
            given(acmeConfig.encryptionKey).willReturn(shortKey)

            // When / Then
            assertThatThrownBy { cryptoHelper.encrypt("anything") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("32 bytes")
        }
    }
}

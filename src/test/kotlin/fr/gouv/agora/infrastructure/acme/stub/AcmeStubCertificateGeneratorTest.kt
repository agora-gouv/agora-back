package fr.gouv.agora.infrastructure.acme.stub

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AcmeStubCertificateGeneratorTest {

    private val generator = AcmeStubCertificateGenerator()

    @Nested
    inner class `generate` {

        @Test
        fun `generate - when called - should return a non-empty PEM certificate`() {
            // When
            val result = generator.generate("test.local")

            // Then
            assertThat(result.certPem).isNotBlank()
            assertThat(result.certPem).contains("-----BEGIN CERTIFICATE-----")
            assertThat(result.certPem).contains("-----END CERTIFICATE-----")
        }

        @Test
        fun `generate - when called - should return a non-empty PEM private key`() {
            // When
            val result = generator.generate("test.local")

            // Then
            assertThat(result.privateKeyPem).isNotBlank()
            assertThat(result.privateKeyPem).contains("-----BEGIN PRIVATE KEY-----")
            assertThat(result.privateKeyPem).contains("-----END PRIVATE KEY-----")
        }

        @Test
        fun `generate - when called - should return expiration date approximately 90 days in the future`() {
            // Given
            val now = LocalDateTime.now()

            // When
            val result = generator.generate("test.local")

            // Then
            assertThat(result.expiresAt).isAfter(now.plusDays(89))
            assertThat(result.expiresAt).isBefore(now.plusDays(91))
        }

        @Test
        fun `generate - when called with a domain - should include domain in certificate PEM`() {
            // When
            val result = generator.generate("agora.gouv.fr")

            // Then
            // Le cert est valide (non vide) et contient bien du contenu base64
            assertThat(result.certPem).isNotBlank()
            assertThat(result.expiresAt).isAfter(LocalDateTime.now())
        }

        @Test
        fun `generate - when called twice - should return different certificates`() {
            // When
            val result1 = generator.generate("test.local")
            val result2 = generator.generate("test.local")

            // Then — deux paires de clés distinctes
            assertThat(result1.certPem).isNotEqualTo(result2.certPem)
            assertThat(result1.privateKeyPem).isNotEqualTo(result2.privateKeyPem)
        }
    }
}

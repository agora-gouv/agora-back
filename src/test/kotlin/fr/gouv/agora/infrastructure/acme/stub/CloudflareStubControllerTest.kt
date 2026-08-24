package fr.gouv.agora.infrastructure.acme.stub

import fr.gouv.agora.config.AcmeConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class CloudflareStubControllerTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @Mock
    private lateinit var stubStore: AcmeStubStore

    @InjectMocks
    private lateinit var controller: CloudflareStubController

    @Nested
    inner class `getZone` {

        @Test
        fun `getZone - when called - should return status active`() {
            // Given
            given(acmeConfig.domain).willReturn("example.gouv.fr")

            // When
            val response = controller.getZone("zone-abc123")

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            val body = response.body!!
            assertThat(body["success"]).isEqualTo(true)
            @Suppress("UNCHECKED_CAST")
            val result = body["result"] as Map<String, Any>
            assertThat(result["id"]).isEqualTo("zone-abc123")
            assertThat(result["status"]).isEqualTo("active")
        }

        @Test
        fun `getZone - when called - should record call in stub store`() {
            // Given
            given(acmeConfig.domain).willReturn("example.gouv.fr")

            // When
            controller.getZone("zone-abc123")

            // Then
            then(stubStore).should().record("GET", "/stub/cloudflare/zones/zone-abc123", "getZone zoneId=zone-abc123")
        }

        @Test
        fun `getZone - when domain is blank - should use stub local as zone name`() {
            // Given
            given(acmeConfig.domain).willReturn("")

            // When
            val response = controller.getZone("zone-abc123")

            // Then
            @Suppress("UNCHECKED_CAST")
            val result = response.body!!["result"] as Map<String, Any>
            assertThat(result["name"]).isEqualTo("stub.local")
        }
    }

    @Nested
    inner class `deployCertificate` {

        @Test
        fun `deployCertificate - when called with valid cert - should return success true`() {
            // Given
            val body = mapOf(
                "certificate" to "-----BEGIN CERTIFICATE-----\nABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\n-----END CERTIFICATE-----",
                "private_key" to "-----BEGIN PRIVATE KEY-----\nKEY\n-----END PRIVATE KEY-----",
                "bundle_method" to "ubiquitous",
            )

            // When
            val response = controller.deployCertificate("zone-abc123", body)

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            val responseBody = response.body!!
            assertThat(responseBody["success"]).isEqualTo(true)
            @Suppress("UNCHECKED_CAST")
            val result = responseBody["result"] as Map<String, Any>
            assertThat(result["status"]).isEqualTo("active")
        }

        @Test
        fun `deployCertificate - when called - should record call in stub store`() {
            // Given
            val certPem = "-----BEGIN CERTIFICATE-----\nABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\n-----END CERTIFICATE-----"
            val body = mapOf("certificate" to certPem)

            // When
            controller.deployCertificate("zone-abc123", body)

            // Then
            then(stubStore).should().record(
                "POST",
                "/stub/cloudflare/zones/zone-abc123/custom_certificates",
                "deployCertificate zoneId=zone-abc123 certPreview=ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890",
            )
        }

        @Test
        fun `deployCertificate - when called - should update lastDeployedCertPreview in stub store`() {
            // Given — utilisation d'une vraie instance pour tester l'assignation du champ
            val realStore = AcmeStubStore()
            val controllerWithRealStore = CloudflareStubController(acmeConfig, realStore)
            val certPem = "-----BEGIN CERTIFICATE-----\nABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrst\n-----END CERTIFICATE-----"
            val body = mapOf("certificate" to certPem)

            // When
            controllerWithRealStore.deployCertificate("zone-abc123", body)

            // Then — preview = premier segment non-header, tronqué à 40 chars
            assertThat(realStore.lastDeployedCertPreview)
                .isEqualTo("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcd")
            assertThat(realStore.lastDeployedAt).isNotNull()
        }
    }
}

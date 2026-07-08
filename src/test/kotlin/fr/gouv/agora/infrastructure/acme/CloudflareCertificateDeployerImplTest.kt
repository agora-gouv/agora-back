package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.infrastructure.acme.repository.CloudflareCertificateDeployerImpl
import fr.gouv.agora.infrastructure.acme.repository.CloudflareDeploymentException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

@ExtendWith(MockitoExtension::class)
class CloudflareCertificateDeployerImplTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @InjectMocks
    private lateinit var deployer: CloudflareCertificateDeployerImpl

    @Captor
    private lateinit var httpEntityCaptor: ArgumentCaptor<HttpEntity<Map<String, String>>>

    private fun stubRestTemplate(response: ResponseEntity<Map<*, *>>) {
        given(
            restTemplate.exchange(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.eq(HttpMethod.PATCH),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.eq(Map::class.java),
            )
        ).willReturn(response)
    }

    @Nested
    inner class Success {

        @Test
        fun `deployCertificate - when response is 200 - should not throw exception`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc")
            given(acmeConfig.cloudflareApiToken).willReturn("my-token")
            stubRestTemplate(ResponseEntity.ok(emptyMap<String, Any>()))

            // When / Then — pas d'exception
            deployer.deployCertificate("cert-pem", "key-pem")
        }

        @Test
        fun `deployCertificate - when called - should send correct payload fields`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc")
            given(acmeConfig.cloudflareApiToken).willReturn("my-token")
            stubRestTemplate(ResponseEntity.ok(emptyMap<String, Any>()))

            // When
            deployer.deployCertificate("my-cert", "my-key")

            // Then — on capture l'HttpEntity envoyée
            org.mockito.BDDMockito.then(restTemplate).should().exchange(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.eq(HttpMethod.PATCH),
                httpEntityCaptor.capture(),
                org.mockito.ArgumentMatchers.eq(Map::class.java),
            )
            val sentBody = httpEntityCaptor.value.body!!
            assertThat(sentBody["certificate"]).isEqualTo("my-cert")
            assertThat(sentBody["private_key"]).isEqualTo("my-key")
            assertThat(sentBody["bundle_method"]).isEqualTo("ubiquitous")
        }

        @Test
        fun `deployCertificate - when called - should send Authorization Bearer header`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc")
            given(acmeConfig.cloudflareApiToken).willReturn("super-secret-token")
            stubRestTemplate(ResponseEntity.ok(emptyMap<String, Any>()))

            // When
            deployer.deployCertificate("cert", "key")

            // Then
            org.mockito.BDDMockito.then(restTemplate).should().exchange(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.eq(HttpMethod.PATCH),
                httpEntityCaptor.capture(),
                org.mockito.ArgumentMatchers.eq(Map::class.java),
            )
            val authHeader = httpEntityCaptor.value.headers.getFirst("Authorization")
            assertThat(authHeader).isEqualTo("Bearer super-secret-token")
        }
    }

    @Nested
    inner class Errors {

        @Test
        fun `deployCertificate - when response is 400 - should throw CloudflareDeploymentException with error body`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc")
            given(acmeConfig.cloudflareApiToken).willReturn("my-token")
            val errorBody = mapOf("errors" to listOf("invalid certificate format"))
            stubRestTemplate(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody))

            // When / Then
            assertThatThrownBy { deployer.deployCertificate("bad-cert", "key") }
                .isInstanceOf(CloudflareDeploymentException::class.java)
                .hasMessageContaining("400")
                .hasMessageContaining("zone-abc")
        }

        @Test
        fun `deployCertificate - when response is 403 - should throw CloudflareDeploymentException`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc")
            given(acmeConfig.cloudflareApiToken).willReturn("bad-token")
            stubRestTemplate(ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("message" to "Unauthorized")))

            // When / Then
            assertThatThrownBy { deployer.deployCertificate("cert", "key") }
                .isInstanceOf(CloudflareDeploymentException::class.java)
                .hasMessageContaining("403")
        }
    }
}

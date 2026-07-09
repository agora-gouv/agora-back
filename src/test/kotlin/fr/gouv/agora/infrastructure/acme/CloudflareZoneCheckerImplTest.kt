package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.infrastructure.acme.repository.CloudflareZoneCheckException
import fr.gouv.agora.infrastructure.acme.repository.CloudflareZoneCheckerImpl
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

@ExtendWith(MockitoExtension::class)
class CloudflareZoneCheckerImplTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @InjectMocks
    private lateinit var checker: CloudflareZoneCheckerImpl

    @Nested
    inner class `getZoneInfo - when Cloudflare API returns success` {

        @Test
        fun `getZoneInfo - when API returns valid zone - should return parsed CloudflareZoneInfo`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc123")
            given(acmeConfig.cloudflareApiToken).willReturn("token-xyz")

            val apiResponse: Map<String, Any> = mapOf(
                "result" to mapOf(
                    "name" to "example.gouv.fr",
                    "status" to "active",
                    "plan" to mapOf("name" to "Enterprise"),
                )
            )
            given(
                restTemplate.exchange(
                    eq("https://api.cloudflare.com/client/v4/zones/zone-abc123"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When
            val result = checker.getZoneInfo()

            // Then
            assertThat(result.zoneId).isEqualTo("zone-abc123")
            assertThat(result.name).isEqualTo("example.gouv.fr")
            assertThat(result.status).isEqualTo("active")
            assertThat(result.plan).isEqualTo("Enterprise")
        }

        @Test
        fun `getZoneInfo - when plan is missing in response - should return unknown for plan`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc123")
            given(acmeConfig.cloudflareApiToken).willReturn("token-xyz")

            val apiResponse: Map<String, Any> = mapOf(
                "result" to mapOf(
                    "name" to "example.gouv.fr",
                    "status" to "active",
                )
            )
            given(
                restTemplate.exchange(
                    eq("https://api.cloudflare.com/client/v4/zones/zone-abc123"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When
            val result = checker.getZoneInfo()

            // Then
            assertThat(result.plan).isEqualTo("unknown")
        }
    }

    @Nested
    inner class `getZoneInfo - when Cloudflare API returns error` {

        @Test
        fun `getZoneInfo - when API body is null - should throw CloudflareZoneCheckException`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc123")
            given(acmeConfig.cloudflareApiToken).willReturn("token-xyz")

            given(
                restTemplate.exchange(
                    eq("https://api.cloudflare.com/client/v4/zones/zone-abc123"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(null, HttpStatus.OK))

            // When / Then
            assertThatThrownBy { checker.getZoneInfo() }
                .isInstanceOf(CloudflareZoneCheckException::class.java)
                .hasMessageContaining("empty body")
        }

        @Test
        fun `getZoneInfo - when result field is missing - should throw CloudflareZoneCheckException`() {
            // Given
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc123")
            given(acmeConfig.cloudflareApiToken).willReturn("token-xyz")

            val apiResponse: Map<String, Any> = mapOf("success" to false)
            given(
                restTemplate.exchange(
                    eq("https://api.cloudflare.com/client/v4/zones/zone-abc123"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When / Then
            assertThatThrownBy { checker.getZoneInfo() }
                .isInstanceOf(CloudflareZoneCheckException::class.java)
                .hasMessageContaining("missing 'result' field")
        }
    }
}

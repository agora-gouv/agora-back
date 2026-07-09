package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.infrastructure.acme.repository.AcmeServerCheckException
import fr.gouv.agora.infrastructure.acme.repository.AcmeServerCheckerImpl
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
class AcmeServerCheckerImplTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @InjectMocks
    private lateinit var checker: AcmeServerCheckerImpl

    @Nested
    inner class `getDirectoryInfo - when ACME server returns success` {

        @Test
        fun `getDirectoryInfo - when API returns full directory - should return parsed AcmeServerDirectoryInfo`() {
            // Given
            given(acmeConfig.serverUrl).willReturn("https://acme.zerossl.com/v2/DV90")

            val apiResponse: Map<String, Any> = mapOf(
                "newAccount" to "https://acme.zerossl.com/v2/DV90/newAccount",
                "newOrder" to "https://acme.zerossl.com/v2/DV90/newOrder",
                "meta" to mapOf("termsOfService" to "https://zerossl.com/terms"),
            )
            given(
                restTemplate.exchange(
                    eq("https://acme.zerossl.com/v2/DV90/directory"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When
            val result = checker.getDirectoryInfo()

            // Then
            assertThat(result.serverUrl).isEqualTo("https://acme.zerossl.com/v2/DV90")
            assertThat(result.newAccountUrl).isEqualTo("https://acme.zerossl.com/v2/DV90/newAccount")
            assertThat(result.newOrderUrl).isEqualTo("https://acme.zerossl.com/v2/DV90/newOrder")
            assertThat(result.termsOfServiceUrl).isEqualTo("https://zerossl.com/terms")
        }

        @Test
        fun `getDirectoryInfo - when serverUrl has trailing slash - should call directory without double slash`() {
            // Given
            given(acmeConfig.serverUrl).willReturn("https://acme.zerossl.com/v2/DV90/")

            val apiResponse: Map<String, Any> = mapOf(
                "newAccount" to "https://acme.zerossl.com/v2/DV90/newAccount",
                "newOrder" to "https://acme.zerossl.com/v2/DV90/newOrder",
            )
            given(
                restTemplate.exchange(
                    eq("https://acme.zerossl.com/v2/DV90/directory"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When
            val result = checker.getDirectoryInfo()

            // Then
            assertThat(result.newAccountUrl).isEqualTo("https://acme.zerossl.com/v2/DV90/newAccount")
            assertThat(result.termsOfServiceUrl).isNull()
        }

        @Test
        fun `getDirectoryInfo - when meta is absent - should return null termsOfServiceUrl`() {
            // Given
            given(acmeConfig.serverUrl).willReturn("https://acme.example.com")

            val apiResponse: Map<String, Any> = mapOf(
                "newAccount" to "https://acme.example.com/newAccount",
                "newOrder" to "https://acme.example.com/newOrder",
            )
            given(
                restTemplate.exchange(
                    eq("https://acme.example.com/directory"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When
            val result = checker.getDirectoryInfo()

            // Then
            assertThat(result.termsOfServiceUrl).isNull()
        }
    }

    @Nested
    inner class `getDirectoryInfo - when ACME server returns error` {

        @Test
        fun `getDirectoryInfo - when API body is null - should throw AcmeServerCheckException`() {
            // Given
            given(acmeConfig.serverUrl).willReturn("https://acme.zerossl.com/v2/DV90")

            given(
                restTemplate.exchange(
                    eq("https://acme.zerossl.com/v2/DV90/directory"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(null, HttpStatus.OK))

            // When / Then
            assertThatThrownBy { checker.getDirectoryInfo() }
                .isInstanceOf(AcmeServerCheckException::class.java)
                .hasMessageContaining("empty body")
        }

        @Test
        fun `getDirectoryInfo - when newAccount field is missing - should throw AcmeServerCheckException`() {
            // Given
            given(acmeConfig.serverUrl).willReturn("https://acme.zerossl.com/v2/DV90")

            val apiResponse: Map<String, Any> = mapOf(
                "newOrder" to "https://acme.zerossl.com/v2/DV90/newOrder",
            )
            given(
                restTemplate.exchange(
                    eq("https://acme.zerossl.com/v2/DV90/directory"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When / Then
            assertThatThrownBy { checker.getDirectoryInfo() }
                .isInstanceOf(AcmeServerCheckException::class.java)
                .hasMessageContaining("missing 'newAccount' field")
        }

        @Test
        fun `getDirectoryInfo - when newOrder field is missing - should throw AcmeServerCheckException`() {
            // Given
            given(acmeConfig.serverUrl).willReturn("https://acme.zerossl.com/v2/DV90")

            val apiResponse: Map<String, Any> = mapOf(
                "newAccount" to "https://acme.zerossl.com/v2/DV90/newAccount",
            )
            given(
                restTemplate.exchange(
                    eq("https://acme.zerossl.com/v2/DV90/directory"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Map::class.java),
                )
            ).willReturn(ResponseEntity(apiResponse, HttpStatus.OK))

            // When / Then
            assertThatThrownBy { checker.getDirectoryInfo() }
                .isInstanceOf(AcmeServerCheckException::class.java)
                .hasMessageContaining("missing 'newOrder' field")
        }
    }
}

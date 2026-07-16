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
class AcmeStubControllerTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @Mock
    private lateinit var stubStore: AcmeStubStore

    @Mock
    private lateinit var nonceStore: AcmeStubNonceStore

    @Mock
    private lateinit var orderStore: AcmeStubOrderStore

    @Mock
    private lateinit var certGenerator: AcmeStubCertificateGenerator

    @InjectMocks
    private lateinit var controller: AcmeStubController

    private fun givenBaseUrl() {
        given(acmeConfig.port).willReturn(8080)
    }

    @Nested
    inner class `directory` {

        @Test
        fun `directory - when called - should return HTTP 200`() {
            // Given
            givenBaseUrl()

            // When
            val response = controller.directory()

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        }

        @Test
        fun `directory - when called - should return all 5 required ACME endpoint URLs`() {
            // Given
            givenBaseUrl()

            // When
            val response = controller.directory()

            // Then
            val body = response.body!!
            assertThat(body).containsKey("newNonce")
            assertThat(body).containsKey("newAccount")
            assertThat(body).containsKey("newOrder")
            assertThat(body).containsKey("revokeCert")
            assertThat(body).containsKey("keyChange")
        }

        @Test
        fun `directory - when called - should return URLs pointing to localhost`() {
            // Given
            givenBaseUrl()

            // When
            val response = controller.directory()

            // Then
            val body = response.body!!
            assertThat(body["newNonce"].toString()).contains("localhost:8080/stub/acme/new-nonce")
            assertThat(body["newAccount"].toString()).contains("localhost:8080/stub/acme/new-account")
            assertThat(body["newOrder"].toString()).contains("localhost:8080/stub/acme/new-order")
        }

        @Test
        fun `directory - when called - should record call in stub store`() {
            // Given
            givenBaseUrl()

            // When
            controller.directory()

            // Then
            then(stubStore).should().record("GET", "/stub/acme/directory", "directory")
        }
    }

    @Nested
    inner class `newAccount` {

        @Test
        fun `newAccount - when called - should return HTTP 201 Created`() {
            // Given
            givenBaseUrl()
            given(nonceStore.generateNonce()).willReturn("test-nonce-abc")

            // When
            val response = controller.newAccount("{}")

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        }

        @Test
        fun `newAccount - when called - should return Location header`() {
            // Given
            givenBaseUrl()
            given(nonceStore.generateNonce()).willReturn("test-nonce-abc")

            // When
            val response = controller.newAccount("{}")

            // Then
            assertThat(response.headers["Location"]).isNotEmpty()
            assertThat(response.headers["Location"]!!.first()).contains("/stub/acme/account/")
        }

        @Test
        fun `newAccount - when called - should return Replay-Nonce header`() {
            // Given
            givenBaseUrl()
            given(nonceStore.generateNonce()).willReturn("test-nonce-abc")

            // When
            val response = controller.newAccount("{}")

            // Then
            assertThat(response.headers["Replay-Nonce"]).containsExactly("test-nonce-abc")
        }

        @Test
        fun `newAccount - when called - should return account status valid`() {
            // Given
            givenBaseUrl()
            given(nonceStore.generateNonce()).willReturn("test-nonce-abc")

            // When
            val response = controller.newAccount("{}")

            // Then
            assertThat(response.body!!["status"]).isEqualTo("valid")
        }
    }

    @Nested
    inner class `newOrder` {

        private fun givenOrderStore() {
            given(orderStore.orders).willReturn(java.util.concurrent.ConcurrentHashMap())
            given(orderStore.authzs).willReturn(java.util.concurrent.ConcurrentHashMap())
            given(orderStore.challengeToOrder).willReturn(java.util.concurrent.ConcurrentHashMap())
        }

        @Test
        fun `newOrder - when called - should return HTTP 201 Created`() {
            // Given
            given(acmeConfig.domain).willReturn("test.local")
            givenBaseUrl()
            givenOrderStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.newOrder("{}")

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        }

        @Test
        fun `newOrder - when called - should return Location header`() {
            // Given
            given(acmeConfig.domain).willReturn("test.local")
            givenBaseUrl()
            givenOrderStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.newOrder("{}")

            // Then
            assertThat(response.headers["Location"]).isNotEmpty()
            assertThat(response.headers["Location"]!!.first()).contains("/stub/acme/order/")
        }

        @Test
        fun `newOrder - when called - should return response with authorizations list`() {
            // Given
            given(acmeConfig.domain).willReturn("test.local")
            givenBaseUrl()
            givenOrderStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.newOrder("{}")

            // Then
            val body = response.body!!
            @Suppress("UNCHECKED_CAST")
            val authzs = body["authorizations"] as List<String>
            assertThat(authzs).hasSize(1)
            assertThat(authzs.first()).contains("/stub/acme/authz/")
        }

        @Test
        fun `newOrder - when called - should return pending status`() {
            // Given
            given(acmeConfig.domain).willReturn("test.local")
            givenBaseUrl()
            givenOrderStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.newOrder("{}")

            // Then
            assertThat(response.body!!["status"]).isEqualTo("pending")
        }

        @Test
        fun `newOrder - when domain is blank - should use stub local`() {
            // Given
            given(acmeConfig.domain).willReturn("")
            givenBaseUrl()
            givenOrderStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.newOrder("{}")

            // Then
            @Suppress("UNCHECKED_CAST")
            val identifiers = response.body!!["identifiers"] as List<Map<String, String>>
            assertThat(identifiers.first()["value"]).isEqualTo("stub.local")
        }
    }

    @Nested
    inner class `challenge` {

        private fun givenChallengeStore() {
            given(orderStore.challengeToOrder).willReturn(java.util.concurrent.ConcurrentHashMap())
        }

        @Test
        fun `challenge - when called - should return status valid`() {
            // Given
            givenChallengeStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.challenge("my-token-xyz", "{}")

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body!!["status"]).isEqualTo("valid")
        }

        @Test
        fun `challenge - when called - should return the token in response`() {
            // Given
            givenChallengeStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.challenge("my-token-xyz", "{}")

            // Then
            assertThat(response.body!!["token"]).isEqualTo("my-token-xyz")
        }

        @Test
        fun `challenge - when called - should return http-01 type`() {
            // Given
            givenChallengeStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            val response = controller.challenge("my-token-xyz", "{}")

            // Then
            assertThat(response.body!!["type"]).isEqualTo("http-01")
        }

        @Test
        fun `challenge - when called - should record call in stub store`() {
            // Given
            givenChallengeStore()
            given(nonceStore.generateNonce()).willReturn("test-nonce")

            // When
            controller.challenge("my-token-xyz", "{}")

            // Then
            then(stubStore).should().record(
                "POST",
                "/stub/acme/challenge/my-token-xyz",
                "challenge token=my-token-xyz status=valid",
            )
        }
    }

    @Nested
    inner class `getCertificate` {

        @Test
        fun `getCertificate - when certificate exists - should return HTTP 200 with PEM body`() {
            // Given — utilisation d'instances réelles pour tester le flux complet
            val realNonceStore = AcmeStubNonceStore()
            val realOrderStore = AcmeStubOrderStore()
            val realCertGenerator = AcmeStubCertificateGenerator()
            val realStubStore = AcmeStubStore()
            val realAcmeConfig = object : AcmeConfig() {}
            val realController = AcmeStubController(
                acmeConfig = realAcmeConfig,
                stubStore = realStubStore,
                nonceStore = realNonceStore,
                orderStore = realOrderStore,
                certGenerator = realCertGenerator,
            )

            // Finaliser un order pour injecter un cert dans le cache
            realOrderStore.orders["order1"] = StubOrder(
                id = "order1",
                domain = "test.local",
                authzId = "authz1",
                challengeToken = "token1",
            )
            val finalizeResponse = realController.finalizeOrder("order1", "{}")
            val certUrl = finalizeResponse.body!!["certificate"] as String
            val certId = certUrl.substringAfterLast("/")

            // When
            val response = realController.getCertificate(certId, "{}")

            // Then
            val bodyString: String = response.body?.toString(Charsets.UTF_8) ?: ""
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(bodyString).isNotBlank()
            assertThat(bodyString).containsIgnoringCase("BEGIN CERTIFICATE")
        }

        @Test
        fun `getCertificate - when certificate does not exist - should return HTTP 404`() {
            // When
            val response = controller.getCertificate("unknown-cert-id", "{}")

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        }
    }

    @Nested
    inner class `getAuthz` {

        @Test
        fun `getAuthz - when authz exists with pending status - should return pending challenge`() {
            // Given
            given(acmeConfig.domain).willReturn("test.local")
            givenBaseUrl()
            given(nonceStore.generateNonce()).willReturn("test-nonce")
            given(orderStore.authzs).willReturn(
                java.util.concurrent.ConcurrentHashMap<String, StubAuthz>().apply {
                    put("authz1", StubAuthz(id = "authz1", orderId = "order1", challengeToken = "tok123", status = "pending"))
                }
            )

            // When
            val response = controller.getAuthz("authz1", "{}")

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body!!["status"]).isEqualTo("pending")
            @Suppress("UNCHECKED_CAST")
            val challenges = response.body!!["challenges"] as List<Map<String, Any>>
            assertThat(challenges.first()["status"]).isEqualTo("pending")
            assertThat(challenges.first()["token"]).isEqualTo("tok123")
        }

        @Test
        fun `getAuthz - when authz status is valid - should return valid challenge`() {
            // Given
            given(acmeConfig.domain).willReturn("test.local")
            givenBaseUrl()
            given(nonceStore.generateNonce()).willReturn("test-nonce")
            given(orderStore.authzs).willReturn(
                java.util.concurrent.ConcurrentHashMap<String, StubAuthz>().apply {
                    put("authz1", StubAuthz(id = "authz1", orderId = "order1", challengeToken = "tok123", status = "valid"))
                }
            )

            // When
            val response = controller.getAuthz("authz1", "{}")

            // Then
            @Suppress("UNCHECKED_CAST")
            val challenges = response.body!!["challenges"] as List<Map<String, Any>>
            assertThat(challenges.first()["status"]).isEqualTo("valid")
        }
    }
}

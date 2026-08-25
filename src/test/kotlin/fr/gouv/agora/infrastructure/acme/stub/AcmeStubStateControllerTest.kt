package fr.gouv.agora.infrastructure.acme.stub

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
import java.time.LocalDateTime
import java.util.Collections

@ExtendWith(MockitoExtension::class)
class AcmeStubStateControllerTest {

    @Mock
    private lateinit var stubStore: AcmeStubStore

    @InjectMocks
    private lateinit var controller: AcmeStubStateController

    @Nested
    inner class `getCalls` {

        @Test
        fun `getCalls - when store is empty - should return empty list`() {
            // Given
            given(stubStore.calls).willReturn(Collections.synchronizedList(mutableListOf()))

            // When
            val response = controller.getCalls()

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).isEmpty()
        }

        @Test
        fun `getCalls - when store has calls - should return all calls`() {
            // Given
            val call1 = AcmeStubStore.StubCall(
                timestamp = LocalDateTime.of(2026, 7, 16, 10, 0),
                method = "GET",
                endpoint = "/stub/acme/directory",
                summary = "directory",
            )
            val call2 = AcmeStubStore.StubCall(
                timestamp = LocalDateTime.of(2026, 7, 16, 10, 1),
                method = "POST",
                endpoint = "/stub/acme/new-account",
                summary = "newAccount id=stub-account-001",
            )
            given(stubStore.calls).willReturn(Collections.synchronizedList(mutableListOf(call1, call2)))

            // When
            val response = controller.getCalls()

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).hasSize(2)
            assertThat(response.body!![0].method).isEqualTo("GET")
            assertThat(response.body!![1].method).isEqualTo("POST")
        }
    }

    @Nested
    inner class `resetCalls` {

        @Test
        fun `resetCalls - when called - should call reset on store`() {
            // When
            val response = controller.resetCalls()

            // Then
            then(stubStore).should().reset()
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).contains("réinitialisé")
        }
    }

    @Nested
    inner class `getLastDeploy` {

        @Test
        fun `getLastDeploy - when no cert deployed yet - should return null values`() {
            // Given
            given(stubStore.lastDeployedCertPreview).willReturn(null)
            given(stubStore.lastDeployedAt).willReturn(null)

            // When
            val response = controller.getLastDeploy()

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body!!["lastDeployedCertPreview"]).isNull()
            assertThat(response.body!!["lastDeployedAt"]).isNull()
        }

        @Test
        fun `getLastDeploy - when cert was deployed - should return preview and timestamp`() {
            // Given
            val deployedAt = LocalDateTime.of(2026, 7, 16, 12, 0)
            given(stubStore.lastDeployedCertPreview).willReturn("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghij")
            given(stubStore.lastDeployedAt).willReturn(deployedAt)

            // When
            val response = controller.getLastDeploy()

            // Then
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body!!["lastDeployedCertPreview"]).isEqualTo("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghij")
            assertThat(response.body!!["lastDeployedAt"]).isEqualTo(deployedAt)
        }
    }
}

package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.repository.AcmeServerChecker
import fr.gouv.agora.usecase.acme.repository.AcmeServerDirectoryInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AcmeServerHealthCheckUseCaseTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @Mock
    private lateinit var acmeServerChecker: AcmeServerChecker

    @InjectMocks
    private lateinit var useCase: AcmeServerHealthCheckUseCase

    @Nested
    inner class `getDirectoryInfo - when ACME is disabled` {

        @Test
        fun `getDirectoryInfo - when ACME_ENABLED is false - should return Disabled without calling checker`() {
            // Given
            given(acmeConfig.enabled).willReturn(false)

            // When
            val result = useCase.getDirectoryInfo()

            // Then
            assertThat(result).isInstanceOf(AcmeServerHealthCheckResult.Disabled::class.java)
            val disabled = result as AcmeServerHealthCheckResult.Disabled
            assertThat(disabled.message).contains("ACME_ENABLED=false")
            then(acmeServerChecker).shouldHaveNoInteractions()
        }

        @Test
        fun `getDirectoryInfo - when ACME_SERVER_INTERACTION_ENABLED is false - should return Disabled without calling checker`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(false)

            // When
            val result = useCase.getDirectoryInfo()

            // Then
            assertThat(result).isInstanceOf(AcmeServerHealthCheckResult.Disabled::class.java)
            val disabled = result as AcmeServerHealthCheckResult.Disabled
            assertThat(disabled.message).contains("ACME_SERVER_INTERACTION_ENABLED=false")
            then(acmeServerChecker).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class `getDirectoryInfo - when ACME is enabled` {

        @Test
        fun `getDirectoryInfo - when checker returns directory info - should return DirectoryInfo result`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
            given(acmeConfig.serverUrl).willReturn("https://acme.zerossl.com/v2/DV90")
            val expectedInfo = AcmeServerDirectoryInfo(
                serverUrl = "https://acme.zerossl.com/v2/DV90",
                newAccountUrl = "https://acme.zerossl.com/v2/DV90/newAccount",
                newOrderUrl = "https://acme.zerossl.com/v2/DV90/newOrder",
                termsOfServiceUrl = "https://zerossl.com/terms",
            )
            given(acmeServerChecker.getDirectoryInfo()).willReturn(expectedInfo)

            // When
            val result = useCase.getDirectoryInfo()

            // Then
            assertThat(result).isInstanceOf(AcmeServerHealthCheckResult.DirectoryInfo::class.java)
            val directoryInfo = result as AcmeServerHealthCheckResult.DirectoryInfo
            assertThat(directoryInfo.info).isEqualTo(expectedInfo)
            assertThat(directoryInfo.info.serverUrl).isEqualTo("https://acme.zerossl.com/v2/DV90")
            assertThat(directoryInfo.info.newAccountUrl).isEqualTo("https://acme.zerossl.com/v2/DV90/newAccount")
            assertThat(directoryInfo.info.newOrderUrl).isEqualTo("https://acme.zerossl.com/v2/DV90/newOrder")
            assertThat(directoryInfo.info.termsOfServiceUrl).isEqualTo("https://zerossl.com/terms")
        }

        @Test
        fun `getDirectoryInfo - when checker returns directory info without ToS - should return DirectoryInfo with null termsOfServiceUrl`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
            given(acmeConfig.serverUrl).willReturn("https://acme.example.com")
            val expectedInfo = AcmeServerDirectoryInfo(
                serverUrl = "https://acme.example.com",
                newAccountUrl = "https://acme.example.com/newAccount",
                newOrderUrl = "https://acme.example.com/newOrder",
                termsOfServiceUrl = null,
            )
            given(acmeServerChecker.getDirectoryInfo()).willReturn(expectedInfo)

            // When
            val result = useCase.getDirectoryInfo()

            // Then
            assertThat(result).isInstanceOf(AcmeServerHealthCheckResult.DirectoryInfo::class.java)
            val directoryInfo = result as AcmeServerHealthCheckResult.DirectoryInfo
            assertThat(directoryInfo.info.termsOfServiceUrl).isNull()
        }

        @Test
        fun `getDirectoryInfo - when checker throws - should propagate exception`() {
            // Given
            given(acmeConfig.enabled).willReturn(true)
            given(acmeConfig.acmeServerInteractionEnabled).willReturn(true)
            given(acmeConfig.serverUrl).willReturn("https://acme.zerossl.com/v2/DV90")
            given(acmeServerChecker.getDirectoryInfo()).willThrow(RuntimeException("ACME server unreachable"))

            // When / Then
            org.junit.jupiter.api.assertThrows<RuntimeException> {
                useCase.getDirectoryInfo()
            }
        }
    }
}

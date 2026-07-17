package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.repository.CloudflareZoneChecker
import fr.gouv.agora.usecase.acme.repository.CloudflareZoneInfo
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
class CloudflareHealthCheckUseCaseTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @Mock
    private lateinit var cloudflareZoneChecker: CloudflareZoneChecker

    @InjectMocks
    private lateinit var useCase: CloudflareHealthCheckUseCase

    @Nested
    inner class `getZoneInfo - when cloudflare interaction is disabled` {

        @Test
        fun `getZoneInfo - when cloudflareInteractionEnabled is false - should return Disabled result without calling checker`() {
            // Given
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(false)

            // When
            val result = useCase.getZoneInfo()

            // Then
            assertThat(result).isInstanceOf(CloudflareHealthCheckResult.Disabled::class.java)
            val disabled = result as CloudflareHealthCheckResult.Disabled
            assertThat(disabled.message).contains("ACME_CLOUDFLARE_INTERACTION_ENABLED=false")
            then(cloudflareZoneChecker).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class `getZoneInfo - when cloudflare interaction is enabled` {

        @Test
        fun `getZoneInfo - when cloudflare returns zone info - should return ZoneInfo result`() {
            // Given
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(true)
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc123")
            val expectedZoneInfo = CloudflareZoneInfo(
                zoneId = "zone-abc123",
                name = "example.gouv.fr",
                status = "active",
                plan = "Enterprise",
            )
            given(cloudflareZoneChecker.getZoneInfo()).willReturn(expectedZoneInfo)

            // When
            val result = useCase.getZoneInfo()

            // Then
            assertThat(result).isInstanceOf(CloudflareHealthCheckResult.ZoneInfo::class.java)
            val zoneInfo = result as CloudflareHealthCheckResult.ZoneInfo
            assertThat(zoneInfo.info).isEqualTo(expectedZoneInfo)
            assertThat(zoneInfo.info.zoneId).isEqualTo("zone-abc123")
            assertThat(zoneInfo.info.name).isEqualTo("example.gouv.fr")
            assertThat(zoneInfo.info.status).isEqualTo("active")
            assertThat(zoneInfo.info.plan).isEqualTo("Enterprise")
        }

        @Test
        fun `getZoneInfo - when cloudflare checker throws - should propagate exception`() {
            // Given
            given(acmeConfig.cloudflareInteractionEnabled).willReturn(true)
            given(acmeConfig.cloudflareZoneId).willReturn("zone-abc123")
            given(cloudflareZoneChecker.getZoneInfo()).willThrow(RuntimeException("Cloudflare API error"))

            // When / Then
            org.junit.jupiter.api.assertThrows<RuntimeException> {
                useCase.getZoneInfo()
            }
        }
    }
}

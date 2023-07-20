package fr.social.gouv.agora.usecase.appVersionControl

import fr.social.gouv.agora.domain.AppPlatform
import fr.social.gouv.agora.usecase.appVersionControl.repository.MinimalAppVersionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class AppVersionControlUseCaseTest {

    @Autowired
    private lateinit var useCase: AppVersionControlUseCase

    @MockBean
    private lateinit var minimalAppVersionRepository: MinimalAppVersionRepository

    @Test
    fun `getAppVersionStatus - when unknown platform - should return INVALID_APP`() {
        // When
        val result = useCase.getAppVersionStatus(
            platform = "quantum-computer",
            versionCode = "42",
        )

        // Then
        assertThat(result).isEqualTo(AppVersionStatus.INVALID_APP)
    }

    @Test
    fun `getAppVersionStatus - when versionCode is not an Int - should return INVALID_APP`() {
        // When
        val result = useCase.getAppVersionStatus(
            platform = "web",
            versionCode = "3.14",
        )

        // Then
        assertThat(result).isEqualTo(AppVersionStatus.INVALID_APP)
    }

    @Test
    fun `getAppVersionStatus - when get requiredVersion lower than versionCode - should return AUTHORIZED`() {
        // Given
        given(minimalAppVersionRepository.getMinimalAppVersion(AppPlatform.ANDROID)).willReturn(7)

        // When
        val result = useCase.getAppVersionStatus(
            platform = "android",
            versionCode = "56",
        )

        // Then
        assertThat(result).isEqualTo(AppVersionStatus.AUTHORIZED)
    }

    @Test
    fun `getAppVersionStatus - when get requiredVersion higher than versionCode - should return UPDATE_REQUIRED`() {
        // Given
        given(minimalAppVersionRepository.getMinimalAppVersion(AppPlatform.IOS)).willReturn(8)

        // When
        val result = useCase.getAppVersionStatus(
            platform = "ios",
            versionCode = "2",
        )

        // Then
        assertThat(result).isEqualTo(AppVersionStatus.UPDATE_REQUIRED)
    }

    @Test
    fun `getAppVersionStatus - when get requiredVersion equals versionCode - should return AUTHORIZED`() {
        // Given
        given(minimalAppVersionRepository.getMinimalAppVersion(AppPlatform.WEB)).willReturn(9)

        // When
        val result = useCase.getAppVersionStatus(
            platform = "web",
            versionCode = "9",
        )

        // Then
        assertThat(result).isEqualTo(AppVersionStatus.AUTHORIZED)
    }

}
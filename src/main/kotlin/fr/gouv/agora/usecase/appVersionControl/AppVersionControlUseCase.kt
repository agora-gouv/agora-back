package fr.gouv.agora.usecase.appVersionControl

import fr.gouv.agora.domain.AppPlatform
import fr.gouv.agora.usecase.appVersionControl.repository.MinimalAppVersionRepository
import org.springframework.stereotype.Service

enum class AppVersionStatus {
    INVALID_APP, UPDATE_REQUIRED, AUTHORIZED
}

@Service
class AppVersionControlUseCase(private val minimalAppVersionRepository: MinimalAppVersionRepository) {

    fun getAppVersionStatus(platform: String, versionCode: String): AppVersionStatus {
        val appPlatform = toAppPlatform(platform) ?: return AppVersionStatus.INVALID_APP
        val versionCodeInt = versionCode.toIntOrNull() ?: return AppVersionStatus.INVALID_APP

        return if (versionCodeInt >= minimalAppVersionRepository.getMinimalAppVersion(appPlatform)) {
            return AppVersionStatus.AUTHORIZED
        } else AppVersionStatus.UPDATE_REQUIRED
    }

    private fun toAppPlatform(platform: String) = when (platform) {
        "android" -> AppPlatform.ANDROID
        "ios" -> AppPlatform.IOS
        "web" -> AppPlatform.WEB
        else -> null
    }

}
package fr.gouv.agora.infrastructure.appVersionControl

import fr.gouv.agora.domain.AppPlatform
import fr.gouv.agora.usecase.appVersionControl.repository.MinimalAppVersionRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class MinimalAppVersionRepositoryImpl : MinimalAppVersionRepository {

    override fun getMinimalAppVersion(appPlatform: AppPlatform): Int {
        return when (appPlatform) {
            AppPlatform.ANDROID -> System.getenv("REQUIRED_ANDROID_VERSION")
            AppPlatform.IOS -> System.getenv("REQUIRED_IOS_VERSION")
            AppPlatform.WEB -> System.getenv("REQUIRED_WEB_VERSION")
        }.toIntOrNull() ?: 0
    }

}
package fr.social.gouv.agora.usecase.appVersionControl.repository

import fr.social.gouv.agora.domain.AppPlatform

interface MinimalAppVersionRepository {
    fun getMinimalAppVersion(appPlatform: AppPlatform): Int
}
package fr.gouv.agora.usecase.appVersionControl.repository

import fr.gouv.agora.domain.AppPlatform

interface MinimalAppVersionRepository {
    fun getMinimalAppVersion(appPlatform: AppPlatform): Int
}
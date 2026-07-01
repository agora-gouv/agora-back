package fr.gouv.agora.usecase.themeHebdo.repository

import fr.gouv.agora.domain.ThemeHebdo

interface CurrentThemeHebdoCacheRepository {
    fun getCurrentThemeHebdo(): CurrentThemeHebdoCacheResult
    fun insertCurrentThemeHebdo(themeHebdo: ThemeHebdo)
}

sealed class CurrentThemeHebdoCacheResult {
    data class CachedCurrentThemeHebdo(val themeHebdo: ThemeHebdo) : CurrentThemeHebdoCacheResult()
    object CacheNotInitialized : CurrentThemeHebdoCacheResult()
}

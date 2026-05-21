package fr.gouv.agora.infrastructure.themeHebdo.repository

import fr.gouv.agora.domain.ThematiqueHebdo
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ThemeHebdoCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val THEME_HEBDO_CACHE_NAME = "themeHebdoCache"
        private const val CACHE_KEY = "themeHebdoList"
    }

    sealed class CacheResult {
        data class CachedThemeHebdoList(val themeHebdoList: List<ThematiqueHebdo>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getThemeHebdoList(): CacheResult {
        val cachedList =
                try {
                    getCache()?.get(CACHE_KEY, List::class.java) as? List<ThematiqueHebdo>
                } catch (e: IllegalStateException) {
                    null
                }
        return cachedList?.let { CacheResult.CachedThemeHebdoList(it) }
                ?: CacheResult.CacheNotInitialized
    }

    fun insertThemeHebdoList(themeHebdoList: List<ThematiqueHebdo>) {
        getCache()?.put(CACHE_KEY, themeHebdoList)
    }

    private fun getCache() = cacheManager.getCache(THEME_HEBDO_CACHE_NAME)
}

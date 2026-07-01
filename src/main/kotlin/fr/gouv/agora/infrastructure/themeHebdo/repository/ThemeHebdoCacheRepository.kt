package fr.gouv.agora.infrastructure.themeHebdo.repository

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.CurrentThemeHebdoCacheRepository
import fr.gouv.agora.usecase.themeHebdo.repository.CurrentThemeHebdoCacheResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ThemeHebdoCacheRepository(@Qualifier("shortTermCacheManager") private val cacheManager: CacheManager) :
    CurrentThemeHebdoCacheRepository {

    companion object {
        private const val THEME_HEBDO_CACHE_NAME = "themeHebdoCache"
        private const val CACHE_KEY = "themeHebdoList"
        private const val CURRENT_THEME_CACHE_KEY = "currentThemeHebdo"
    }

    sealed class CacheResult {
        data class CachedThemeHebdoList(val themeHebdoList: List<ThemeHebdo>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getThemeHebdoList(): CacheResult {
        val cachedList =
                try {
                    getCache()?.get(CACHE_KEY, List::class.java) as? List<ThemeHebdo>
                } catch (e: IllegalStateException) {
                    null
                }
        return cachedList?.let { CacheResult.CachedThemeHebdoList(it) }
                ?: CacheResult.CacheNotInitialized
    }

    fun insertThemeHebdoList(themeHebdoList: List<ThemeHebdo>) {
        getCache()?.put(CACHE_KEY, themeHebdoList)
    }

    override fun getCurrentThemeHebdo(): CurrentThemeHebdoCacheResult {
        val cached = try {
            getCache()?.get(CURRENT_THEME_CACHE_KEY, ThemeHebdo::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return cached?.let { CurrentThemeHebdoCacheResult.CachedCurrentThemeHebdo(it) }
            ?: CurrentThemeHebdoCacheResult.CacheNotInitialized
    }

    override fun insertCurrentThemeHebdo(themeHebdo: ThemeHebdo) {
        getCache()?.put(CURRENT_THEME_CACHE_KEY, themeHebdo)
    }

    private fun getCache() = cacheManager.getCache(THEME_HEBDO_CACHE_NAME)
}

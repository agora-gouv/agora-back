package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ThematiqueCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val THEMATIQUE_CACHE_NAME = "thematiqueCache"
        private const val CACHE_KEY = "thematiqueList"
    }

    sealed class CacheListResult {
        data class CachedThematiqueList(val thematiqueListDTO: List<ThematiqueDTO>) : CacheListResult()
        object CacheNotInitialized : CacheListResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getThematiqueList(): CacheListResult {
        val cachedListDTO = try {
            getCache()?.get(CACHE_KEY, List::class.java) as? List<ThematiqueDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return cachedListDTO?.let { CacheListResult.CachedThematiqueList(it) } ?: CacheListResult.CacheNotInitialized
    }

    fun insertThematiqueList(thematiqueListDTO: List<ThematiqueDTO>) {
        getCache()?.put(CACHE_KEY, thematiqueListDTO)
    }

    private fun getCache() = cacheManager.getCache(THEMATIQUE_CACHE_NAME)

}
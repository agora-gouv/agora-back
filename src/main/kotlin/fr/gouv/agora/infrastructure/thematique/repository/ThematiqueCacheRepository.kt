package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ThematiqueCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val THEMATIQUE_CACHE_NAME = "thematiqueCache"
        private const val CACHE_KEY = "thematiqueList"
    }

    sealed class CacheListResult {
        data class CachedThematiqueList(val thematiqueList: List<Thematique>) : CacheListResult()
        object CacheNotInitialized : CacheListResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getThematiqueList(): CacheListResult {
        val cachedListDTO = try {
            getCache()?.get(CACHE_KEY, List::class.java) as? List<Thematique>
        } catch (e: IllegalStateException) {
            null
        }
        return cachedListDTO?.let { CacheListResult.CachedThematiqueList(it) } ?: CacheListResult.CacheNotInitialized
    }

    fun insertThematiqueList(thematiqueList: List<Thematique>) {
        getCache()?.put(CACHE_KEY, thematiqueList)
    }

    private fun getCache() = cacheManager.getCache(THEMATIQUE_CACHE_NAME)

}

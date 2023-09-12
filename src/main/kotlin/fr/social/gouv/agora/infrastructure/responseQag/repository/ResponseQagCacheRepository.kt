package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ResponseQagCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val RESPONSE_QAG_CACHE_NAME = "responseQagCache"
        private const val RESPONSE_QAG_CACHE_KEY = "responseQagCacheList"
    }

    sealed class CacheResult {
        data class CachedResponseQagList(val allResponseQagDTO: List<ResponseQagDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun initializeCache(responseQagListDTO: List<ResponseQagDTO>) {
        getCache()?.put(RESPONSE_QAG_CACHE_KEY, responseQagListDTO)
    }

    fun getAllResponseQagList(): CacheResult {
        return when (val allResponseQagDTO = getAllResponseQagDTOFromCache()) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedResponseQagList(allResponseQagDTO)
        }
    }

    private fun getCache() = cacheManager.getCache(RESPONSE_QAG_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getAllResponseQagDTOFromCache(): List<ResponseQagDTO>? {
        return try {
            getCache()?.get(RESPONSE_QAG_CACHE_KEY, List::class.java) as? List<ResponseQagDTO>
        } catch (e: IllegalStateException) {
            null
        }
    }
}

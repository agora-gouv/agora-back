package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class QagCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val QAG_CACHE_NAME = "qagCache"
        private const val ALL_QAG_CACHE_KEY = "All"
    }

    fun isInitialized(): Boolean {
        return getAllQagDTOFromCache() != null
    }

    fun initializeCache(allQagDTO: List<QagDTO>) {
        getCache()?.put(ALL_QAG_CACHE_KEY, allQagDTO)
    }

    fun getAllQagList() = getAllQagDTOFromCache() ?: emptyList()

    fun insertQag(qagDTO: QagDTO) {
        getAllQagDTOFromCache()?.let { allQagDTO ->
            initializeCache(allQagDTO + qagDTO)
        } ?: throw IllegalStateException("SupportQag cache has not been initialized")
    }

    private fun getCache() = cacheManager.getCache(QAG_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getAllQagDTOFromCache(): List<QagDTO>? {
        return try {
            getCache()?.get(ALL_QAG_CACHE_KEY, List::class.java) as? List<QagDTO>
        } catch (e: IllegalStateException) {
            null
        }
    }
}
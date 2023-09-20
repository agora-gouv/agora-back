package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SupportQagCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        const val SUPPORT_QAG_CACHE_NAME = "SupportQagCache"
        const val ALL_SUPPORT_QAG_CACHE_KEY = "supportQagList"
    }

    sealed class CacheResult {
        data class CachedSupportQagList(val allSupportQagDTO: List<SupportQagDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun initializeCache(allSupportQagDTO: List<SupportQagDTO>) {
        getCache()?.put(ALL_SUPPORT_QAG_CACHE_KEY, allSupportQagDTO)
    }

    fun getAllSupportQagList(): CacheResult {
        return when (val allSupportQagDTO = getAllSupportQagDTOFromCache()) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedSupportQagList(allSupportQagDTO)
        }
    }

    @Throws(IllegalStateException::class)
    fun insertSupportQag(supportQagDTO: SupportQagDTO) {
        getAllSupportQagDTOFromCache()?.let { allSupportQagDTO ->
            initializeCache(allSupportQagDTO + supportQagDTO)
        } ?: throw IllegalStateException("SupportQag cache has not been initialized")
    }

    @Throws(IllegalStateException::class)
    fun deleteSupportQag(qagId: UUID, userId: UUID) {
        getAllSupportQagDTOFromCache()?.let { allSupportQagDTO ->
            val userSupportQagDTO = allSupportQagDTO
                .find { supportQagDTO -> supportQagDTO.qagId == qagId && supportQagDTO.userId == userId }

            if (userSupportQagDTO != null) {
                initializeCache(allSupportQagDTO.filterNot { supportQagDTO -> userSupportQagDTO.id == supportQagDTO.id })
            }
        } ?: throw IllegalStateException("SupportQag cache has not been initialized")
    }

    @Throws(IllegalStateException::class)
    fun deleteSupportListByQagId(qagId: UUID) {
        getAllSupportQagDTOFromCache()?.let { allSupportQagDTO ->
            val filteredSupportQagDTO = allSupportQagDTO.filterNot { supportQagDTO -> supportQagDTO.qagId == qagId }
            if (filteredSupportQagDTO.size < allSupportQagDTO.size) {
                initializeCache(filteredSupportQagDTO)
            }
        } ?: throw IllegalStateException("SupportQag cache has not been initialized")
    }

    private fun getCache() = cacheManager.getCache(SUPPORT_QAG_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getAllSupportQagDTOFromCache(): List<SupportQagDTO>? {
        return try {
            getCache()?.get(ALL_SUPPORT_QAG_CACHE_KEY, List::class.java) as? List<SupportQagDTO>
        } catch (e: IllegalStateException) {
            null
        }
    }

}
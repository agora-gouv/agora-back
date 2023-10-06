package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SupportQagCacheRepository(
    @Deprecated("should use shortTermCacheManager")
    private val cacheManager: CacheManager,
    @Qualifier("shortTermCacheManager") private val shortTermCacheManager: CacheManager,
) {

    companion object {
        const val SUPPORT_QAG_CACHE_NAME = "SupportQagCache"
        @Deprecated("")
        const val ALL_SUPPORT_QAG_CACHE_KEY = "supportQagList"
    }

    @Deprecated("")
    sealed class CacheResult {
        data class CachedSupportQagList(val allSupportQagDTO: List<SupportQagDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Deprecated("")
    fun initializeCache(allSupportQagDTO: List<SupportQagDTO>) {
        getCache()?.put(ALL_SUPPORT_QAG_CACHE_KEY, allSupportQagDTO)
    }

    @Deprecated("")
    fun getAllSupportQagList(): CacheResult {
        return when (val allSupportQagDTO = getAllSupportQagDTOFromCache()) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedSupportQagList(allSupportQagDTO)
        }
    }

    @Deprecated("")
    @Throws(IllegalStateException::class)
    fun insertSupportQag(supportQagDTO: SupportQagDTO) {
        getAllSupportQagDTOFromCache()?.let { allSupportQagDTO ->
            initializeCache(allSupportQagDTO + supportQagDTO)
        } ?: throw IllegalStateException("SupportQag cache has not been initialized")
    }

    @Deprecated("")
    @Throws(IllegalStateException::class)
    fun deleteSupportQag(userId: UUID, qagId: UUID) {
        getAllSupportQagDTOFromCache()?.let { allSupportQagDTO ->
            val userSupportQagDTO = allSupportQagDTO
                .find { supportQagDTO -> supportQagDTO.qagId == qagId && supportQagDTO.userId == userId }

            if (userSupportQagDTO != null) {
                initializeCache(allSupportQagDTO.filterNot { supportQagDTO -> userSupportQagDTO.id == supportQagDTO.id })
            }
        } ?: throw IllegalStateException("SupportQag cache has not been initialized")
    }

    @Deprecated("")
    @Throws(IllegalStateException::class)
    fun deleteSupportListByQagId(qagId: UUID) {
        getAllSupportQagDTOFromCache()?.let { allSupportQagDTO ->
            val filteredSupportQagDTO = allSupportQagDTO.filterNot { supportQagDTO -> supportQagDTO.qagId == qagId }
            if (filteredSupportQagDTO.size < allSupportQagDTO.size) {
                initializeCache(filteredSupportQagDTO)
            }
        } ?: throw IllegalStateException("SupportQag cache has not been initialized")
    }

    @Suppress("UNCHECKED_CAST")
    fun getUserSupportedQagIds(userId: UUID): List<String>? {
        return try {
            getShortTermCache()?.get(userId.toString(), List::class.java) as? List<String>
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun initUserSupportedQagIds(userId: UUID, userSupportedQagIds: List<String>) {
        getShortTermCache()?.put(userId.toString(), userSupportedQagIds)
    }

    fun addSupportedQagIds(userId: UUID, qagId: String) {
        getUserSupportedQagIds(userId)?.let { userSupportedQagIds ->
            initUserSupportedQagIds(userId, userSupportedQagIds + qagId)
        }
    }

    fun removeSupportedQagIds(userId: UUID, qagId: String) {
        getUserSupportedQagIds(userId)?.let { userSupportedQagIds ->
            val filteredQagIds = userSupportedQagIds.filter { supportedQagId -> supportedQagId != qagId }
            if (filteredQagIds.size < userSupportedQagIds.size) {
                initUserSupportedQagIds(userId, filteredQagIds)
            }
        }
    }

    @Deprecated("")
    private fun getCache() = cacheManager.getCache(SUPPORT_QAG_CACHE_NAME)
    private fun getShortTermCache() = shortTermCacheManager.getCache(SUPPORT_QAG_CACHE_NAME)

    @Deprecated("")
    @Suppress("UNCHECKED_CAST")
    private fun getAllSupportQagDTOFromCache(): List<SupportQagDTO>? {
        return try {
            getCache()?.get(ALL_SUPPORT_QAG_CACHE_KEY, List::class.java) as? List<SupportQagDTO>
        } catch (e: IllegalStateException) {
            null
        }
    }

}
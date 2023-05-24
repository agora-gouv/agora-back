package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID_STRING
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class QagCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val QAG_CACHE_NAME = "qagCache"
        private const val QAG_POPULAR_CACHE_KEY = "qagPopularCacheList"
        private const val QAG_LATEST_CACHE_KEY = "qagLatestCacheList"
    }

    sealed class CacheResult {
        data class CachedQag(val qagDTO: QagDTO) : CacheResult()
        object CachedQagNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    sealed class CacheListResult {
        data class CachedQagList(val qagListDTO: List<QagDTO>) : CacheListResult()
        object CacheNotInitialized : CacheListResult()
    }

    fun getQag(qagUUID: UUID): CacheResult {
        val qagDTO = try {
            getCache()?.get(qagUUID.toString(), QagDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (qagDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            NOT_FOUND_UUID_STRING -> CacheResult.CachedQagNotFound
            else -> CacheResult.CachedQag(qagDTO)
        }
    }

    fun insertQag(qagUUID: UUID, qagDTO: QagDTO?) {
        getCache()?.put(qagUUID.toString(), qagDTO ?: createQagInfoNotFound())
    }

    @Suppress("UNCHECKED_CAST")
    fun getQagPopularList(thematiqueId: UUID?): CacheListResult {
        val qagPopularList = try {
            getCache()?.get(buildQagCacheKey(thematiqueId, QAG_POPULAR_CACHE_KEY), List::class.java) as? List<QagDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return when (qagPopularList) {
            null -> CacheListResult.CacheNotInitialized
            else -> CacheListResult.CachedQagList(qagPopularList)
        }
    }

    fun insertQagPopularList(thematiqueId: UUID?, qagPopularList: List<QagDTO>?) {
        getCache()?.put(
            buildQagCacheKey(thematiqueId, QAG_POPULAR_CACHE_KEY),
            qagPopularList ?: emptyList<QagDTO>(),
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun getQagLatestList(thematiqueId: UUID?): CacheListResult {
        val qagLatestList = try {
            getCache()?.get(buildQagCacheKey(thematiqueId, QAG_LATEST_CACHE_KEY), List::class.java) as? List<QagDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return when (qagLatestList) {
            null -> CacheListResult.CacheNotInitialized
            else -> CacheListResult.CachedQagList(qagLatestList)
        }
    }

    fun insertQagLatestList(thematiqueId: UUID?, qagLatestList: List<QagDTO>?) {
        getCache()?.put(
            buildQagCacheKey(thematiqueId, QAG_LATEST_CACHE_KEY),
            qagLatestList ?: emptyList<QagDTO>(),
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun getQagSupportedList(thematiqueId: UUID?, userId: UUID): CacheListResult {
        val qagSupportedList = try {
            getCache()?.get(buildQagCacheKey(thematiqueId, userId.toString()), List::class.java) as? List<QagDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return when (qagSupportedList) {
            null -> CacheListResult.CacheNotInitialized
            else -> CacheListResult.CachedQagList(qagSupportedList)
        }
    }

    fun insertQagSupportedList(thematiqueId: UUID?, qagSupportedList: List<QagDTO>?, userId: UUID) {
        getCache()?.put(
            buildQagCacheKey(thematiqueId, userId.toString()),
            qagSupportedList ?: emptyList<QagDTO>(),
        )
    }

    fun deleteQagSupportedList(thematiqueId: UUID, userId: UUID) {
        getCache()?.evict(buildQagCacheKey(thematiqueId, userId.toString()))
        getCache()?.evict(userId.toString())
    }

    private fun getCache() = cacheManager.getCache(QAG_CACHE_NAME)

    private fun createQagInfoNotFound(): QagDTO {
        return QagDTO(
            id = NOT_FOUND_UUID,
            title = "",
            description = "",
            postDate = Date(0),
            status = 0,
            username = "",
            thematiqueId = NOT_FOUND_UUID,
            userId = NOT_FOUND_UUID,
        )
    }

    private fun buildQagCacheKey(thematiqueId: UUID?, key: String) = thematiqueId?.let { "$thematiqueId/$key" } ?: key
}
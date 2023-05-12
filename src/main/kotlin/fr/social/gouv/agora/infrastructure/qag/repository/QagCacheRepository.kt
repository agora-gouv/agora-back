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

    sealed class CachePopularListResult {
        data class CachedQagList(val qagListDTO: List<QagDTO>) : CachePopularListResult()
        object CacheNotInitialized : CachePopularListResult()
    }

    sealed class CacheLatestListResult {
        data class CachedQagList(val qagListDTO: List<QagDTO>) : CacheLatestListResult()
        object CacheNotInitialized : CacheLatestListResult()
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
    fun getQagPopularList(thematiqueId: UUID?): CachePopularListResult {
        val qagPopularList = try {
            getCache()?.get(thematiqueId?.toString() ?: QAG_POPULAR_CACHE_KEY, List::class.java) as? List<QagDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return when (qagPopularList) {
            null -> CachePopularListResult.CacheNotInitialized
            else -> CachePopularListResult.CachedQagList(qagPopularList)
        }
    }

    fun insertQagLatestList(thematiqueId: UUID?, qagLatestList: List<QagDTO>?) {
        getCache()?.put(
            thematiqueId?.toString() ?: QAG_LATEST_CACHE_KEY,
            qagLatestList ?: emptyList<QagDTO>(),
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun getQagLatestList(thematiqueId: UUID?): CacheLatestListResult {
        val qagLatestList = try {
            getCache()?.get(thematiqueId?.toString() ?: QAG_LATEST_CACHE_KEY, List::class.java) as? List<QagDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return when (qagLatestList) {
            null -> CacheLatestListResult.CacheNotInitialized
            else -> CacheLatestListResult.CachedQagList(qagLatestList)
        }
    }

    fun insertQagPopularList(thematiqueId: UUID?, qagPopularList: List<QagDTO>?) {
        getCache()?.put(
            thematiqueId?.toString() ?: QAG_POPULAR_CACHE_KEY,
            qagPopularList ?: emptyList<QagDTO>(),
        )
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
        )
    }
}
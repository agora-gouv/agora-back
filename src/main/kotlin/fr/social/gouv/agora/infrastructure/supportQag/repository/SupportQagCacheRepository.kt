package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SupportQagCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        const val SUPPORT_QAG_COUNT_CACHE_NAME = "SupportQagCountCache"

        const val SUPPORT_QAG_CACHE_NAME = "SupportQagCache"
        const val SUPPORT_QAG_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    sealed class CacheResult {
        data class CachedSupportQag(val supportQagDTO: SupportQagDTO) : CacheResult()
        object CachedSupportQagNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getSupportCount(qagId: UUID): Int? {
        return try {
            getCountCache()?.get(qagId.toString(), String::class.java)?.toIntOrNull()
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun insertSupportCount(qagId: UUID, supportCount: Int) {
        getCountCache()?.put(qagId.toString(), supportCount.toString())
    }

    fun getSupportQag(qagId: UUID, userId: String): CacheResult {
        val supportQagDTO = try {
            getCache()?.get(buildSupportQagCacheKey(qagId, userId), SupportQagDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (supportQagDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            SUPPORT_QAG_NOT_FOUND_ID -> CacheResult.CachedSupportQagNotFound
            else -> CacheResult.CachedSupportQag(supportQagDTO)
        }
    }

    fun insertSupportQag(
        qagId: UUID,
        userId: String,
        supportQagDTO: SupportQagDTO?,
    ) {
        getCache()?.put(
            buildSupportQagCacheKey(qagId, userId),
            supportQagDTO ?: createSupportQagNotFound(),
        )

        getCountCache()?.evict(qagId.toString())
    }

    private fun getCountCache() = cacheManager.getCache(SUPPORT_QAG_COUNT_CACHE_NAME)
    private fun getCache() = cacheManager.getCache(SUPPORT_QAG_CACHE_NAME)

    private fun buildSupportQagCacheKey(qagId: UUID, userId: String) = "$qagId/$userId"

    private fun createSupportQagNotFound() = SupportQagDTO(
        id = UUID.fromString(SUPPORT_QAG_NOT_FOUND_ID),
        userId = "",
        qagId = UUID.fromString(SUPPORT_QAG_NOT_FOUND_ID),
    )

}
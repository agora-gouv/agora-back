package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID_STRING
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
        data class CachedResponseQag(val responseQagDTO: ResponseQagDTO) : CacheResult()
        object CachedResponseQagNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    sealed class CacheListResult {
        data class CachedResponseQagList(val responseQagListDTO: List<ResponseQagDTO>) : CacheListResult()
        object CacheNotInitialized : CacheListResult()
    }

    fun getResponseQag(qagId: UUID): CacheResult {
        val responseQagDTO = try {
            getCache()?.get(qagId.toString(), ResponseQagDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (responseQagDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            NOT_FOUND_UUID_STRING -> CacheResult.CachedResponseQagNotFound
            else -> CacheResult.CachedResponseQag(responseQagDTO)
        }
    }

    fun insertResponseQag(qagId: UUID, responseQagDTO: ResponseQagDTO) {
        getCache()?.put(qagId.toString(), responseQagDTO)
    }

    fun deleteResponseQag(qagId: UUID) {
        getCache()?.put(qagId.toString(), createResponseQagNotFound())
    }

    private fun getCache() = cacheManager.getCache(RESPONSE_QAG_CACHE_NAME)

    private fun createResponseQagNotFound(): ResponseQagDTO {
        return ResponseQagDTO(
            id = NOT_FOUND_UUID,
            author = "",
            authorPortraitUrl = "",
            authorDescription = "",
            responseDate = Date(0),
            videoUrl = "",
            transcription = "",
            qagId = NOT_FOUND_UUID,
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun getResponseQagList(): CacheListResult {
        val responseQagList = try {
            getCache()?.get(RESPONSE_QAG_CACHE_KEY, List::class.java) as? List<ResponseQagDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return when (responseQagList) {
            null -> CacheListResult.CacheNotInitialized
            else -> CacheListResult.CachedResponseQagList(responseQagList)
        }
    }

    fun insertResponseQagList(responseQagListDTO: List<ResponseQagDTO>?) {
        getCache()?.put(
            RESPONSE_QAG_CACHE_KEY,
            responseQagListDTO ?: emptyList<ResponseQagDTO>(),
        )
    }
}

package fr.gouv.agora.infrastructure.headerQag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheResult
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class HeaderQagCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : HeaderQagCacheRepository {

    companion object {
        private const val HEADER_CACHE_NAME = "headersQag"
        private const val HEADER_QAG_NOT_FOUND_VALUE = ""
    }

    override fun getHeader(filterType: String): HeaderQagCacheResult {
        return when (val cacheContent = getCache()?.get(filterType, String::class.java)) {
            null -> HeaderQagCacheResult.HeaderQagCacheNotInitialized
            HEADER_QAG_NOT_FOUND_VALUE -> HeaderQagCacheResult.HeaderQagNotFound
            else -> try {
                HeaderQagCacheResult.CachedHeaderQag(objectMapper.readValue(cacheContent, HeaderQag::class.java))
            } catch (e: Exception) {
                HeaderQagCacheResult.HeaderQagCacheNotInitialized
            }
        }
    }
    override fun initHeaderNotFound(filterType: String) {
        getCache()?.put(filterType, HEADER_QAG_NOT_FOUND_VALUE)
    }
    override fun initHeader(filterType: String, headerQag: HeaderQag) {
        getCache()?.put(
            filterType,
            objectMapper.writeValueAsString(headerQag)
        )
    }

    private fun getCache() = cacheManager.getCache(HEADER_CACHE_NAME)
}
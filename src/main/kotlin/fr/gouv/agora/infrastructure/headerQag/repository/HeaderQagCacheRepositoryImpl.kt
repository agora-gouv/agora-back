package fr.gouv.agora.infrastructure.headerQag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheRepository
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
    }

    override fun getHeader(filterType: String): HeaderQag? {
        return try {
            getCache()?.get(filterType, HeaderQag::class.java)
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun initHeader(filterType: String, headerQag: HeaderQag) {
        getCache()?.put(
            filterType,
            objectMapper.writeValueAsString(headerQag)
        )
    }

    private fun getCache() = cacheManager.getCache(HEADER_CACHE_NAME)
}
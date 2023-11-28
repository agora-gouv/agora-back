package fr.gouv.agora.infrastructure.header.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.Header
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class HeaderCacheRepositoryImpl(
    @Qualifier("eternalCacheManager") private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : HeaderCacheRepository {

    companion object {
        private const val HEADER_CACHE_NAME = "headers"
    }

    override fun getHeader(filterType: String): Header? {
        return try {
            getCache()?.get(filterType, Header::class.java)
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun initHeader(filterType: String, header: Header) {
        getCache()?.put(
            filterType,
            objectMapper.writeValueAsString(header)
        )
    }

    override fun evictHeader(filterType: String) {
        getCache()?.evict(filterType)
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(HEADER_CACHE_NAME)
}
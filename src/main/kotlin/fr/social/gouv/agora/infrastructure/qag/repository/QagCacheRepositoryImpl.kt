package fr.social.gouv.agora.infrastructure.qag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.usecase.qag.repository.QagCacheRepository
import fr.social.gouv.agora.usecase.qag.repository.QagCacheResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QagCacheRepositoryImpl(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : QagCacheRepository {

    companion object {
        private const val QAG_CACHE = "qag"
        private const val QAG_NOT_FOUND_VALUE = ""
    }

    override fun getQag(qagId: String): QagCacheResult {
        return when (val cacheContent = getCache()?.get(qagId, String::class.java)) {
            null -> QagCacheResult.QagCacheNotInitialized
            QAG_NOT_FOUND_VALUE -> QagCacheResult.QagNotFount
            else -> QagCacheResult.CachedQag(objectMapper.readValue(cacheContent, Qag::class.java))
        }
    }

    override fun initQagNotFound(qagId: String) {
        getCache()?.put(qagId, QAG_NOT_FOUND_VALUE)
    }

    override fun initQag(qag: Qag) {
        getCache()?.put(qag.id, objectMapper.writeValueAsString(qag))
    }

    override fun evictQag(qagId: String) {
        getCache()?.evict(qagId)
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(QAG_CACHE)
}
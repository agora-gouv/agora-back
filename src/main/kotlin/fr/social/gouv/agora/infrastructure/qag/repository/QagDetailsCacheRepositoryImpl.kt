package fr.social.gouv.agora.infrastructure.qag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.social.gouv.agora.domain.QagDetails
import fr.social.gouv.agora.usecase.qag.repository.QagDetailsCacheRepository
import fr.social.gouv.agora.usecase.qag.repository.QagDetailsCacheResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QagDetailsCacheRepositoryImpl(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : QagDetailsCacheRepository {

    companion object {
        private const val QAG_CACHE = "qag"
        private const val QAG_NOT_FOUND_VALUE = ""
    }

    override fun getQag(qagId: String): QagDetailsCacheResult {
        return when (val cacheContent = getCache()?.get(qagId, String::class.java)) {
            null -> QagDetailsCacheResult.QagDetailsCacheNotInitialized
            QAG_NOT_FOUND_VALUE -> QagDetailsCacheResult.QagDetailsNotFount
            else -> QagDetailsCacheResult.CachedQagDetails(objectMapper.readValue(cacheContent, QagDetails::class.java))
        }
    }

    override fun initQagNotFound(qagId: String) {
        getCache()?.put(qagId, QAG_NOT_FOUND_VALUE)
    }

    override fun initQag(qagDetails: QagDetails) {
        getCache()?.put(qagDetails.id, objectMapper.writeValueAsString(qagDetails))
    }

    override fun evictQag(qagId: String) {
        getCache()?.evict(qagId)
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(QAG_CACHE)
}
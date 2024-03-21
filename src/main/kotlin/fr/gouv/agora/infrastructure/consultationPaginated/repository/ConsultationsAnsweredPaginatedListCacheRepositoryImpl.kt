package fr.gouv.agora.infrastructure.consultationPaginated.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.usecase.consultationPaginated.ConsultationAnsweredPaginatedList
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationAnsweredPaginatedListCacheRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationsAnsweredPaginatedListCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationAnsweredPaginatedListCacheRepository {

    companion object {
        private const val CONSULTATIONS_ANSWERED_PAGINATED_CACHE_NAME_PREFIX = "consultationsAnsweredPaginated"
    }

    override fun getConsultationAnsweredPage(userId: String, pageNumber: Int): ConsultationAnsweredPaginatedList? {
        return try {
            getCache(userId)?.get("$pageNumber", String::class.java)?.let { cacheContent ->
                objectMapper.readValue(cacheContent, ConsultationAnsweredPaginatedList::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun initConsultationAnsweredPage(
        userId: String,
        pageNumber: Int,
        content: ConsultationAnsweredPaginatedList?,
    ) {
        getCache(userId)?.put("$pageNumber", objectMapper.writeValueAsString(content))
    }

    override fun clearCache(userId: String) {
        getCache(userId)?.clear()
    }

    private fun getCache(userId: String) =
        cacheManager.getCache("$CONSULTATIONS_ANSWERED_PAGINATED_CACHE_NAME_PREFIX$userId")
}
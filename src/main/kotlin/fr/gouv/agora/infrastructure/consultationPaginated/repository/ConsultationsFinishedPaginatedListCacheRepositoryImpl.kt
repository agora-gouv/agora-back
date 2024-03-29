package fr.gouv.agora.infrastructure.consultationPaginated.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationsFinishedPaginatedListCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationFinishedPaginatedListCacheRepository {

    companion object {
        private const val CONSULTATIONS_FINISHED_PAGINATED_CACHE_NAME = "consultationsFinishedPaginated"
    }

    override fun getConsultationFinishedPage(pageNumber: Int): ConsultationFinishedPaginatedList? {
        return try {
            getCache()?.get("$pageNumber", String::class.java)?.let { cacheContent ->
                objectMapper.readValue(cacheContent, ConsultationFinishedPaginatedList::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun initConsultationFinishedPage(
        pageNumber: Int,
        content: ConsultationFinishedPaginatedList?,
    ) {
        getCache()?.put("$pageNumber", objectMapper.writeValueAsString(content))
    }

    override fun clearCache() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(CONSULTATIONS_FINISHED_PAGINATED_CACHE_NAME)
}
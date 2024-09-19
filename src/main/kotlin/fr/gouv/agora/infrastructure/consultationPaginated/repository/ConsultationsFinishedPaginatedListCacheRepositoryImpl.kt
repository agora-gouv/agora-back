package fr.gouv.agora.infrastructure.consultationPaginated.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class ConsultationsFinishedPaginatedListCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationFinishedPaginatedListCacheRepository {

    companion object {
        private const val CONSULTATIONS_FINISHED_PAGINATED_CACHE_NAME = "consultationsFinishedPaginated"
    }

    override fun getConsultationFinishedPage(pageNumber: Int, territory: Territoire?): ConsultationFinishedPaginatedList? {
        val key = getKey(pageNumber, territory?.value)

        return try {
            getCache()?.get(key, String::class.java)?.let { cacheContent ->
                objectMapper.readValue(cacheContent, ConsultationFinishedPaginatedList::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun initConsultationFinishedPage(
        pageNumber: Int,
        content: ConsultationFinishedPaginatedList?,
        territory: Territoire?,
    ) {
        val key = getKey(pageNumber, territory?.value)

        getCache()?.put(key, objectMapper.writeValueAsString(content))
    }

    override fun clearCache() {
        getCache()?.clear()
    }

    private fun getKey(pageNumber: Int, territory: String?): String {
        return territory?.let {
            "$territory-$pageNumber"
        } ?: "$pageNumber"
    }

    private fun getCache() = cacheManager.getCache(CONSULTATIONS_FINISHED_PAGINATED_CACHE_NAME)
}

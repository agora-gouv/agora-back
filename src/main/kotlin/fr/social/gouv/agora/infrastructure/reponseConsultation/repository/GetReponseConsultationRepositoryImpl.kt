package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.GetReponseConsultationRepositoryImpl.Companion.REPONSE_CONSULTATION_CACHE_NAME
import fr.social.gouv.agora.usecase.reponseConsultation.GetReponseConsultationRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
@CacheConfig(cacheNames = [REPONSE_CONSULTATION_CACHE_NAME])
class GetReponseConsultationRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val mapper: ReponseConsultationMapper,
    private val cacheManager: CacheManager,
) : GetReponseConsultationRepository {

    companion object {
        const val REPONSE_CONSULTATION_CACHE_NAME = "reponseConsultationCache"
    }

    override fun getConsultationResponses(consultationId: String): List<ReponseConsultation> {
        return try {
            val consultationUUID = UUID.fromString(consultationId)
            val consultationResponseDtoList = getConsultationResponsesFromCache(consultationUUID)
                ?: getConsultationResponsesFromDatabase(consultationUUID)

            consultationResponseDtoList.map { dto ->
                mapper.toDomain(dto)
            }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getCache() = cacheManager.getCache(REPONSE_CONSULTATION_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getConsultationResponsesFromCache(consultationUUID: UUID): List<ReponseConsultationDTO>? {
        return getCache()?.get(consultationUUID.toString(), List::class.java) as? List<ReponseConsultationDTO>
    }

    private fun getConsultationResponsesFromDatabase(consultationUUID: UUID): List<ReponseConsultationDTO> {
        val consultationResponseDtoList = databaseRepository.getConsultationResponses(consultationUUID)
        getCache()?.put(consultationUUID.toString(), consultationResponseDtoList)
        return consultationResponseDtoList
    }

}
package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.ReponseConsultationCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetReponseConsultationRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetReponseConsultationRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val cacheRepository: ReponseConsultationCacheRepository,
    private val mapper: ReponseConsultationMapper,
) : GetReponseConsultationRepository {

    override fun getConsultationResponses(consultationId: String): List<ReponseConsultation> {
        return try {
            val consultationUUID = UUID.fromString(consultationId)
            val dtoList = when (val cacheResult = cacheRepository.getReponseConsultationList(consultationUUID)) {
                CacheResult.CacheNotInitialized -> getConsultationResponsesFromDatabase(consultationUUID)
                CacheResult.CacheReponseConsultationNotFound -> emptyList()
                is CacheResult.CacheReponseConsultation -> cacheResult.reponseConsultationList
            }
            dtoList.map(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getConsultationResponsesFromDatabase(consultationUUID: UUID): List<ReponseConsultationDTO> {
        val consultationResponseDtoList = databaseRepository.getConsultationResponses(consultationUUID)
        cacheRepository.insertReponseConsultationList(consultationUUID, consultationResponseDtoList)
        return consultationResponseDtoList
    }

}
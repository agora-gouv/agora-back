package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.ReponseConsultationCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetConsultationResponseRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val cacheRepository: ReponseConsultationCacheRepository,
    private val mapper: ReponseConsultationMapper,
) : GetConsultationResponseRepository {

    override fun getConsultationResponses(consultationId: String): List<ReponseConsultation> {
        return getConsultationResponseDTOList(consultationId).map(mapper::toDomain)
    }

    override fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean {
        return try {
            val userUUID = UUID.fromString(userId)
            getConsultationResponseDTOList(consultationId).any { consultationResponseDTO ->
                consultationResponseDTO.userId == userUUID
            }
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean> {
        // TODO tests
        val userUUID = try {
            UUID.fromString(userId)
        } catch (e: IllegalArgumentException) {
            return emptyMap()
        }

        val consultationUUIDs = consultationIds.mapNotNull { consultationId ->
            try {
                UUID.fromString(consultationId)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        val answeredConsultationList =
            databaseRepository.getAnsweredConsultations(consultationUUIDs, userUUID).map { consultationUUID ->
                consultationUUID.toString()
            }
        return consultationIds.associateWith { consultationId ->
            answeredConsultationList.contains(consultationId)
        }
    }

    private fun getConsultationResponseDTOList(consultationId: String): List<ReponseConsultationDTO> {
        return try {
            val consultationUUID = UUID.fromString(consultationId)
            when (val cacheResult = cacheRepository.getReponseConsultationList(consultationUUID)) {
                CacheResult.CacheNotInitialized -> getConsultationResponsesFromDatabase(consultationUUID)
                CacheResult.CacheReponseConsultationNotFound -> emptyList()
                is CacheResult.CacheReponseConsultation -> cacheResult.reponseConsultationList
            }
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
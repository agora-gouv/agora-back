package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.ReponseConsultationCacheRepository.CacheResult
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
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
        return userId.toUuidOrNull()?.let { userUUID ->
            consultationIds
                .mapNotNull { consultationId -> consultationId.toUuidOrNull() }
                .takeIf { it.isNotEmpty() }
                ?.let { consultationUUIDs ->
                    val answeredConsultationList = databaseRepository.getAnsweredConsultations(
                        consultationIDs = consultationUUIDs,
                        userId = userUUID,
                    ).map { consultationUUID -> consultationUUID.toString() }

                    consultationIds.associateWith { consultationId -> answeredConsultationList.contains(consultationId) }
                }
        } ?: emptyMap()
    }

    override fun getUsersAnsweredConsultation(consultationId: String): List<String> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getUsersAnsweredConsultation(consultationUUID).map { it.toString() }
        } ?: emptyList()
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
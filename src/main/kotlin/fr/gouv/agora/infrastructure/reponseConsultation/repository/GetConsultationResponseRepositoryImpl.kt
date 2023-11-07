package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.reponseConsultation.repository.ReponseConsultationCacheRepository.CacheResult
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetConsultationResponseRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val cacheRepository: ReponseConsultationCacheRepository,
    private val mapper: ReponseConsultationMapper,
) : GetConsultationResponseRepository {

    @Deprecated("Should use getConsultationResponsesCount instead for better performances")
    override fun getConsultationResponses(consultationId: String): List<ReponseConsultation> {
        return getConsultationResponseDTOList(consultationId).map(mapper::toDomain)
    }

    override fun getParticipantCount(consultationId: String): Int {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getParticipantCount(consultationId = consultationUUID)
        } ?: 0
    }

    override fun getConsultationResponsesCount(consultationId: String): List<ResponseConsultationCount> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getConsultationResponsesCount(consultationId = consultationUUID).map(mapper::toDomain)
        } ?: emptyList()
    }

    override fun getParticipantDemographicInfo(consultationId: String): DemographicInfoCount {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            mapper.toDomain(
                genderCount = databaseRepository.getConsultationGender(consultationUUID),
                ageRangeCount = databaseRepository.getConsultationYearOfBirth(consultationUUID),
                departmentCount = databaseRepository.getConsultationDepartment(consultationUUID),
                cityTypeCount = databaseRepository.getConsultationCityType(consultationUUID),
                jobCategoryCount = databaseRepository.getConsultationJobCategory(consultationUUID),
                voteFrequencyCount = databaseRepository.getConsultationVoteFrequency(consultationUUID),
                publicMeetingFrequencyCount = databaseRepository.getConsultationPublicMeetingFrequency(consultationUUID),
                consultationFrequencyCount = databaseRepository.getConsultationConsultationFrequency(consultationUUID),
            )
        } ?: DemographicInfoCount(
            genderCount = emptyMap(),
            ageRangeCount = emptyMap(),
            departmentCount = emptyMap(),
            cityTypeCount = emptyMap(),
            jobCategoryCount = emptyMap(),
            voteFrequencyCount = emptyMap(),
            publicMeetingFrequencyCount = emptyMap(),
            consultationFrequencyCount = emptyMap(),
        )
    }

    override fun getParticipantDemographicInfoByChoices(consultationId: String): DemographicInfoCountByChoices {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            mapper.toDomain(
                genderCount = databaseRepository.getConsultationGenderByChoice(consultationUUID),
                ageRangeCount = databaseRepository.getConsultationYearOfBirthByChoice(consultationUUID),
                departmentCount = databaseRepository.getConsultationDepartmentByChoice(consultationUUID),
                cityTypeCount = databaseRepository.getConsultationCityTypeByChoice(consultationUUID),
                jobCategoryCount = databaseRepository.getConsultationJobCategoryByChoice(consultationUUID),
                voteFrequencyCount = databaseRepository.getConsultationVoteFrequencyByChoice(consultationUUID),
                publicMeetingFrequencyCount = databaseRepository.getConsultationPublicMeetingFrequencyByChoice(
                    consultationUUID
                ),
                consultationFrequencyCount = databaseRepository.getConsultationConsultationFrequencyByChoice(
                    consultationUUID
                ),
            )
        } ?: DemographicInfoCountByChoices(emptyMap())
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
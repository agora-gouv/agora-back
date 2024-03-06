package fr.gouv.agora.infrastructure.consultationResponse.repository

import fr.gouv.agora.domain.DemographicInfoCount
import fr.gouv.agora.domain.DemographicInfoCountByChoices
import fr.gouv.agora.domain.ReponseConsultation
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.infrastructure.consultationResponse.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.consultationResponse.repository.ReponseConsultationCacheRepository.CacheResult
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
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

    override fun getConsultationResponsesCount(consultationId: String): List<ResponseConsultationCount> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getConsultationResponsesCount(consultationId = consultationUUID).map(mapper::toDomain)
        } ?: emptyList()
    }

    override fun deleteConsultationResponses(consultationId: String) {
        consultationId.toUuidOrNull()?.let(databaseRepository::deleteConsultationResponses)
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

    override fun getParticipantDemographicInfoByChoices(questionId: String): DemographicInfoCountByChoices {
        return questionId.toUuidOrNull()?.let { questionUUID ->
            mapper.toDomain(
                genderCount = databaseRepository.getConsultationGenderByChoice(questionUUID),
                ageRangeCount = databaseRepository.getConsultationYearOfBirthByChoice(questionUUID),
                departmentCount = databaseRepository.getConsultationDepartmentByChoice(questionUUID),
                cityTypeCount = databaseRepository.getConsultationCityTypeByChoice(questionUUID),
                jobCategoryCount = databaseRepository.getConsultationJobCategoryByChoice(questionUUID),
                voteFrequencyCount = databaseRepository.getConsultationVoteFrequencyByChoice(questionUUID),
                publicMeetingFrequencyCount = databaseRepository.getConsultationPublicMeetingFrequencyByChoice(
                    questionUUID
                ),
                consultationFrequencyCount = databaseRepository.getConsultationConsultationFrequencyByChoice(
                    questionUUID
                ),
            )
        } ?: DemographicInfoCountByChoices(emptyMap())
    }

    override fun deleteUserConsultationResponses(userIDs: List<String>) {
        databaseRepository.deleteUserConsultationResponses(userIDs.mapNotNull { it.toUuidOrNull() })
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
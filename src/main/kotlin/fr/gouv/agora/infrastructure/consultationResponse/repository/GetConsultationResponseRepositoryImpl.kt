package fr.gouv.agora.infrastructure.consultationResponse.repository

import fr.gouv.agora.domain.DemographicInfoCount
import fr.gouv.agora.domain.DemographicInfoCountByChoices
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Component

@Component
class GetConsultationResponseRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val mapper: ReponseConsultationMapper,
) : GetConsultationResponseRepository {

    override fun getConsultationResponsesCount(consultationId: String): List<ResponseConsultationCount> {
        return databaseRepository.getConsultationResponsesCount(consultationId = consultationId).map(mapper::toDomain)

    }

    override fun deleteConsultationResponses(consultationId: String) {
        databaseRepository.deleteConsultationResponsesWithoutText(consultationId)
        databaseRepository.anonymizeConsultationResponsesWithText(consultationId)
    }

    override fun getParticipantDemographicInfo(consultationId: String): DemographicInfoCount {
        return mapper.toDomain(
            genderCount = databaseRepository.getConsultationGender(consultationId),
            ageRangeCount = databaseRepository.getConsultationYearOfBirth(consultationId),
            departmentCount = databaseRepository.getConsultationDepartment(consultationId),
            cityTypeCount = databaseRepository.getConsultationCityType(consultationId),
            jobCategoryCount = databaseRepository.getConsultationJobCategory(consultationId),
            voteFrequencyCount = databaseRepository.getConsultationVoteFrequency(consultationId),
            publicMeetingFrequencyCount = databaseRepository.getConsultationPublicMeetingFrequency(consultationId),
            consultationFrequencyCount = databaseRepository.getConsultationConsultationFrequency(consultationId),
        )
    }

    override fun getParticipantDemographicInfoByChoices(questionId: String): DemographicInfoCountByChoices {
        return mapper.toDomain(
            genderCount = databaseRepository.getConsultationGenderByChoice(questionId),
            ageRangeCount = databaseRepository.getConsultationYearOfBirthByChoice(questionId),
            departmentCount = databaseRepository.getConsultationDepartmentByChoice(questionId),
            cityTypeCount = databaseRepository.getConsultationCityTypeByChoice(questionId),
            jobCategoryCount = databaseRepository.getConsultationJobCategoryByChoice(questionId),
            voteFrequencyCount = databaseRepository.getConsultationVoteFrequencyByChoice(questionId),
            publicMeetingFrequencyCount = databaseRepository.getConsultationPublicMeetingFrequencyByChoice(
                questionId
            ),
            consultationFrequencyCount = databaseRepository.getConsultationConsultationFrequencyByChoice(
                questionId
            ),
        )
    }

    override fun deleteUserConsultationResponses(userIDs: List<String>) {
        databaseRepository.deleteUserConsultationResponses(userIDs.mapNotNull { it.toUuidOrNull() })
    }
}

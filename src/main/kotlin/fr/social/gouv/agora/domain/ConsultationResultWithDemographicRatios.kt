package fr.social.gouv.agora.domain

import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo

data class ConsultationResultWithDemographicInfo(
    val consultation: ConsultationInfo,
    val participantCount: Int,
    val results: List<QuestionResultWithDemographicInfo>,
    val demographicInfo: DemographicInfo,
)

data class QuestionResultWithDemographicInfo(
    val question: Question,
    val responses: List<ChoiceResultWithDemographicInfo>,
)

data class ChoiceResultWithDemographicInfo(
    val choixPossible: ChoixPossible,
    val ratio: Double,
    val demographicInfo: DemographicInfo,
)

data class DemographicInfo(
    val genderCount: Map<Gender?, Int>,
    val yearOfBirthCount: Map<Int?, Int>,
    val departmentCount: Map<Department?, Int>,
    val cityTypeCount: Map<CityType?, Int>,
    val jobCategoryCount: Map<JobCategory?, Int>,
    val voteFrequencyCount: Map<Frequency?, Int>,
    val publicMeetingFrequencyCount: Map<Frequency?, Int>,
    val consultationFrequencyCount: Map<Frequency?, Int>,
)
package fr.gouv.agora.domain

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo

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
    val countAndRatio: CountAndRatio,
    val demographicInfo: DemographicInfo,
)

data class DemographicInfo(
    val genderCount: Map<Gender?, CountAndRatio>,
    val yearOfBirthCount: Map<Int?, CountAndRatio>,
    val departmentCount: Map<Department?, CountAndRatio>,
    val cityTypeCount: Map<CityType?, CountAndRatio>,
    val jobCategoryCount: Map<JobCategory?, CountAndRatio>,
    val voteFrequencyCount: Map<Frequency?, CountAndRatio>,
    val publicMeetingFrequencyCount: Map<Frequency?, CountAndRatio>,
    val consultationFrequencyCount: Map<Frequency?, CountAndRatio>,
)

data class CountAndRatio(
    val count: Int,
    val ratio: Double,
)

package fr.gouv.agora.domain

data class ReponseConsultation(
    val id: String,
    val questionId: String,
    val choiceId: String,
    val participationId: String,
    val userId: String,
    val responseText: String?,
)

data class ResponseConsultationCount(
    val questionId: String,
    val choiceId: String,
    val responseCount: Int,
)

data class DemographicInfoCountByChoices(
    val choiceDemographicInfoMap: Map<String, DemographicInfoCount>
)

data class DemographicInfoCount(
    val genderCount: Map<Gender?, Int>,
    val ageRangeCount: Map<AgeRange?, Int>,
    val departmentCount: Map<Department?, Int>,
    val cityTypeCount: Map<CityType?, Int>,
    val jobCategoryCount: Map<JobCategory?, Int>,
    val voteFrequencyCount: Map<Frequency?, Int>,
    val publicMeetingFrequencyCount: Map<Frequency?, Int>,
    val consultationFrequencyCount: Map<Frequency?, Int>,
)

data class ReponseConsultationInserting(
    val questionId: String,
    val choiceIds: List<String>?,
    val responseText: String,
)

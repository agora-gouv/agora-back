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

data class ResponseConsultationCountWithDemographicInfo(
    val questionId: String,
    val choiceId: String,
    val responseCount: Int,
    val gender: Gender?,
    val yearOfBirth: Int?,
    val department: Department?,
    val cityType: CityType?,
    val jobCategory: JobCategory?,
    val voteFrequency: Frequency?,
    val publicMeetingFrequency: Frequency?,
    val consultationFrequency: Frequency?,
)

data class ReponseConsultationInserting(
    val questionId: String,
    val choiceIds: List<String>?,
    val responseText: String,
)

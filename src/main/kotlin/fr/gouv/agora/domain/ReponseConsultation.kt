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

data class ReponseConsultationInserting(
    val questionId: String,
    val choiceIds: List<String>?,
    val responseText: String,
)

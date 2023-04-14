package fr.social.gouv.agora.domain

data class ReponseConsultation(
    val id: String,
    val questionId: String,
    val choiceId: String,
    val participationId: String,
)

data class ReponseConsultationInserting(
    val consultationId: String,
    val questionId: String,
    val choiceIds: List<String>?,
    val responseText: String,
    val participationId: String
)

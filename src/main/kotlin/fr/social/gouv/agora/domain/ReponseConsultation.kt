package fr.social.gouv.agora.domain

data class ReponseConsultation(
    val id: String,
    val consultationId: String,
    val questionId: String,
    val choiceIds: List<String>?,
    val responseText: String,
)

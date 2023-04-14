package fr.social.gouv.agora.domain

data class ConsultationUpdate(
    val status: ConsultationStatus,
    val description: String,
)
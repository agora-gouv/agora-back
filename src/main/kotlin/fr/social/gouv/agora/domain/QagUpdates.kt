package fr.social.gouv.agora.domain

data class QagUpdates(
    val qagId: String,
    val newQagStatus: QagStatus,
    val userId: String,
)

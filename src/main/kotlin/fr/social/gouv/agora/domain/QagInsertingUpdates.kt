package fr.social.gouv.agora.domain

data class QagInsertingUpdates(
    val qagId: String,
    val newQagStatus: QagStatus,
    val userId: String,
)

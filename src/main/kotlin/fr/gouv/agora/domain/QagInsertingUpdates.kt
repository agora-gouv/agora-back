package fr.gouv.agora.domain

data class QagInsertingUpdates(
    val qagId: String,
    val newQagStatus: QagStatus,
    val userId: String,
    val reason: String?,
    val shouldDelete: Boolean,
)

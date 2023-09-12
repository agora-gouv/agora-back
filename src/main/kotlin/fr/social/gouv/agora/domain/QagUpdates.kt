package fr.social.gouv.agora.domain

import java.util.*

data class QagUpdates(
    val qagId: String,
    val qagStatus: QagStatus,
    val userId: String,
    val moderatedDate: Date,
)

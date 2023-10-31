package fr.gouv.agora.usecase.qag.repository

import fr.gouv.agora.domain.QagStatus
import java.util.*

data class QagInfo(
    val id: String,
    val thematiqueId: String,
    val title: String,
    val description: String,
    val date: Date,
    val status: QagStatus,
    val username: String,
    val userId: String,
)

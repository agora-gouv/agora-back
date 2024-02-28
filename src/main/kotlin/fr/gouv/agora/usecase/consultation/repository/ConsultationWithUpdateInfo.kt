package fr.gouv.agora.usecase.consultation.repository

import java.util.*

data class ConsultationWithUpdateInfo(
    val id: String,
    val title: String,
    val coverUrl: String,
    val thematiqueId: String,
    val updateDate: Date,
    val updateLabel: String?,
)

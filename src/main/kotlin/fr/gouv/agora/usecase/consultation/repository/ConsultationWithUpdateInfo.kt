package fr.gouv.agora.usecase.consultation.repository

import java.time.LocalDateTime

data class ConsultationWithUpdateInfo(
    val id: String,
    val title: String,
    val coverUrl: String,
    val thematiqueId: String,
    val endDate: LocalDateTime,
    val updateDate: LocalDateTime,
    val updateLabel: String?,
)

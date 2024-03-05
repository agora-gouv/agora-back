package fr.gouv.agora.domain

import java.util.*

data class ConsultationPreviewOngoing(
    val id: String,
    val title: String,
    val coverUrl: String,
    val thematique: Thematique,
    val endDate: Date,
    val highlightLabel: String?,
)

data class ConsultationPreviewFinished(
    val id: String,
    val title: String,
    val coverUrl: String,
    val thematique: Thematique,
    val step: ConsultationStatus,
    val updateLabel: String?,
)

package fr.social.gouv.agora.domain

import java.util.*

sealed class ConsultationPreview {
    abstract val id: String
    abstract val title: String
    abstract val coverUrl: String
    abstract val endDate: Date
    abstract val thematique: Thematique
}

data class ConsultationPreviewOngoing(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val endDate: Date,
    override val thematique: Thematique,
    val hasAnswered: Boolean,
) : ConsultationPreview()


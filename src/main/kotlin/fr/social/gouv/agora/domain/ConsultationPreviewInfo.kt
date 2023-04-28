package fr.social.gouv.agora.domain

import java.util.*

sealed class ConsultationPreviewInfo {
    abstract val id: String
    abstract val title: String
    abstract val coverUrl: String
    abstract val endDate: Date
    abstract val thematiqueId: String
}

data class ConsultationPreviewOngoingInfo(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val endDate: Date,
    override val thematiqueId: String,
    val hasAnswered: Boolean,
) : ConsultationPreviewInfo()


package fr.social.gouv.agora.domain

import java.util.*

sealed class ConsultationPreviewInfo {
    abstract val id: String
    abstract val title: String
    abstract val coverUrl: String
    abstract val thematiqueId: String
}

data class ConsultationPreviewOngoingInfo(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val thematiqueId: String,
    val endDate: Date,
) : ConsultationPreviewInfo()

data class ConsultationPreviewFinishedInfo(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val thematiqueId: String,
) : ConsultationPreviewInfo()

data class ConsultationPreviewAnsweredInfo(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val thematiqueId: String,
) : ConsultationPreviewInfo()

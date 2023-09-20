package fr.social.gouv.agora.domain

import java.util.*

sealed class ConsultationPreviewInfo {
    abstract val id: String
    abstract val title: String
    abstract val coverUrl: String
    abstract val thematiqueId: String
    abstract val startDate: Date
    abstract val endDate: Date
}

data class ConsultationPreviewOngoingInfo(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val thematiqueId: String,
    override val startDate: Date,
    override val endDate: Date,
) : ConsultationPreviewInfo()

data class ConsultationPreviewFinishedInfo(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val thematiqueId: String,
    override val startDate: Date,
    override val endDate: Date,
) : ConsultationPreviewInfo()

data class ConsultationPreviewAnsweredInfo(
    override val id: String,
    override val title: String,
    override val coverUrl: String,
    override val thematiqueId: String,
    override val startDate: Date,
    override val endDate: Date,
) : ConsultationPreviewInfo()

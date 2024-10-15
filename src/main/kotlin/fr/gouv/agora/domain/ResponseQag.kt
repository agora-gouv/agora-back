package fr.gouv.agora.domain

import java.util.Date

sealed class ResponseQag {
    abstract val author: String
    abstract val authorPortraitUrl: String
    abstract val responseDate: Date
    abstract val feedbackQuestion: String
    abstract val qagId: String
}

data class ResponseQagVideo(
    override val author: String,
    override val authorPortraitUrl: String,
    override val responseDate: Date,
    override val feedbackQuestion: String,
    override val qagId: String,
    val authorDescription: String,
    val videoUrl: String,
    val videoTitle: String,
    val videoWidth: Int,
    val videoHeight: Int,
    val transcription: String,
    val additionalInfo: ResponseQagAdditionalInfo?,
) : ResponseQag()

data class ResponseQagText(
    override val author: String,
    override val authorPortraitUrl: String,
    override val responseDate: Date,
    override val feedbackQuestion: String,
    override val qagId: String,
    val responseLabel: String,
    val responseText: String,
) : ResponseQag()

data class ResponseQagAdditionalInfo(
    val additionalInfoTitle: String,
    val additionalInfoDescription: String,
)

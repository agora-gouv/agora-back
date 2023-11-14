package fr.gouv.agora.domain

import java.util.*

sealed class ResponseQag {
    abstract val feedbackQuestion: String
    abstract val qagId: String
}

data class ResponseQagVideo(
    val author: String,
    val authorPortraitUrl: String,
    val authorDescription: String,
    val responseDate: Date,
    val videoUrl: String,
    val videoWidth: Int,
    val videoHeight: Int,
    val transcription: String,
    override val feedbackQuestion: String,
    override val qagId: String,
) : ResponseQag()

data class ResponseQagText(
    val responseLabel: String,
    val responseText: String,
    override val feedbackQuestion: String,
    override val qagId: String,
) : ResponseQag()
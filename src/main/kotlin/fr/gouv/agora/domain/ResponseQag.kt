package fr.gouv.agora.domain

import java.util.*

data class ResponseQag(
    val id: String,
    val author: String,
    val authorPortraitUrl: String,
    val authorDescription: String,
    val responseDate: Date,
    val videoUrl: String,
    val videoWidth: Int,
    val videoHeight: Int,
    val transcription: String,
    val feedbackQuestion: String,
    val qagId: String,
)
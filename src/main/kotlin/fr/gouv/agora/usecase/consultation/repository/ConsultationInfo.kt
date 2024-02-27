package fr.gouv.agora.usecase.consultation.repository

import java.util.*

data class ConsultationInfo(
    val id: String,
    val title: String,
    val coverUrl: String,
    val detailsCoverUrl: String,
    val startDate: Date,
    val endDate: Date,
    val questionCount: String,
    val estimatedTime: String,
    val participantCountGoal: Int,
    val description: String,
    val tipsDescription: String,
    val thematiqueId: String,
)

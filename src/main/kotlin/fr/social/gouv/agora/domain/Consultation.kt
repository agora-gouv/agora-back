package fr.social.gouv.agora.domain

import java.util.*

data class Consultation(
    val id: String,
    val title: String,
    val coverUrl: String,
    val abstract: String,
    val startDate: Date?,
    val endDate: Date,
    val questionCount: String,
    val estimatedTime: String,
    val participantCountGoal: Int,
    val description: String,
    val tipsDescription: String,
    val thematique: Thematique,
)
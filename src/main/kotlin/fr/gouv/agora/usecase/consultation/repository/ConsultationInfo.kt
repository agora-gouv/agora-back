package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.Thematique
import java.time.LocalDateTime

data class ConsultationInfo(
    val id: String,
    val title: String,
    val coverUrl: String,
    val detailsCoverUrl: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val questionCount: String,
    val estimatedTime: String,
    val participantCountGoal: Int,
    val description: String,
    val tipsDescription: String,
    val thematique: Thematique,
) {
    constructor(): this("", "", "", "", LocalDateTime.MIN, LocalDateTime.MIN, "", "", 0, "", "", Thematique())
}

package fr.social.gouv.agora.usecase.consultation.repository


import java.util.*

interface ConsultationRepository {
    fun getConsultation(consultationId: String): ConsultationInfo?
}

 data class ConsultationInfo(
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
    val thematiqueId: String,
)






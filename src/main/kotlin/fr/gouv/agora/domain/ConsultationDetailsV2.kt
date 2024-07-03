package fr.gouv.agora.domain

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo

data class ConsultationDetailsV2(
    val consultation: ConsultationInfo,
    val update: ConsultationUpdateInfoV2,
    val feedbackStats: FeedbackConsultationUpdateStats?,
    val history: List<ConsultationUpdateHistory>?,
)

data class ConsultationDetailsV2WithInfo(
    val consultation: ConsultationInfo,
    val thematique: Thematique,
    val update: ConsultationUpdateInfoV2,
    val feedbackStats: FeedbackConsultationUpdateStats?,
    val history: List<ConsultationUpdateHistory>?,
    val participantCount: Int,
    val isUserFeedbackPositive: Boolean?,
)

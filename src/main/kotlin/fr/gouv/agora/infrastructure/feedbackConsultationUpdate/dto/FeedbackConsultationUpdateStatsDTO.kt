package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto

import org.springframework.data.rest.core.config.Projection

@Projection(types = [FeedbackConsultationUpdateStatsDTO::class])
interface FeedbackConsultationUpdateStatsDTO {
    val hasPositiveValue: Int
    val responseCount: Int
}
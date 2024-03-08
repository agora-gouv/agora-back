package fr.gouv.agora.domain

data class FeedbackConsultationUpdateResults(
    val userResponse: Boolean,
    val stats: FeedbackConsultationUpdateStats?,
)

data class FeedbackConsultationUpdateStats(
    val positiveRatio: Int,
    val negativeRatio: Int,
    val responseCount: Int,
)

data class FeedbackConsultationUpdateInserting(
    val userId: String,
    val consultationId: String,
    val consultationUpdateId: String,
    val isPositive: Boolean,
)
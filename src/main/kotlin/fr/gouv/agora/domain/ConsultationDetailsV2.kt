package fr.gouv.agora.domain

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo

data class ConsultationDetailsV2(
    val consultation: ConsultationInfo,
    val update: ConsultationUpdateInfoV2,
    val feedbackStats: FeedbackConsultationUpdateStats?,
    val history: List<ConsultationUpdateHistory>?,
) {
    private fun isLancementUpdate(): Boolean {
        return update.hasQuestionsInfo
    }

    private fun isResultatUpdate(): Boolean {
        return update.hasParticipationInfo
    }

    fun hasQuestionsOrParticipationInfos(): Boolean {
        return isResultatUpdate() || isLancementUpdate()
    }

    fun getConsultationId(): String {
        return consultation.id
    }
}

data class ConsultationDetailsV2WithInfo(
    val consultation: ConsultationInfo,
    val update: ConsultationUpdateInfoV2,
    val feedbackStats: FeedbackConsultationUpdateStats?,
    val history: List<ConsultationUpdateHistory>?,
    val participantCount: Int,
    val isUserFeedbackPositive: Boolean?,
)

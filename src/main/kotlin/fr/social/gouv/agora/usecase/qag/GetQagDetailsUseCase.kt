package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagDetails
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.QagWithUserData
import fr.social.gouv.agora.usecase.feedbackQag.FeedbackQagUseCase
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Service

@Service
class GetQagDetailsUseCase(
    private val qagDetailsAggregate: QagDetailsAggregate,
    private val supportQagRepository: GetSupportQagRepository,
    private val feedbackQagUseCase: FeedbackQagUseCase,
) {

    fun getQagDetails(qagId: String, userId: String): QagResult {
        return qagDetailsAggregate.getQag(qagId = qagId)?.let { qag ->
            when (qag.status) {
                QagStatus.ARCHIVED -> QagResult.QagNotFound
                QagStatus.MODERATED_REJECTED -> QagResult.QagRejectedStatus
                QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED, QagStatus.SELECTED_FOR_RESPONSE -> if (qag.status == QagStatus.OPEN && userId != qag.userId) {
                    QagResult.QagNotFound
                } else {
                    QagResult.Success(
                        QagWithUserData(
                            qagDetails = qag,
                            canShare = qag.status == QagStatus.MODERATED_ACCEPTED || qag.status == QagStatus.SELECTED_FOR_RESPONSE,
                            canSupport = qag.status == QagStatus.OPEN || qag.status == QagStatus.MODERATED_ACCEPTED,
                            canDelete = qag.userId == userId && qag.status != QagStatus.SELECTED_FOR_RESPONSE,
                            isAuthor = qag.userId == userId,
                            isSupportedByUser = isSupportedByUser(qagDetails = qag, userId = userId),
                            hasGivenFeedback = hasGivenFeedback(qagDetails = qag, userId = userId),
                        )
                    )
                }
            }
        } ?: QagResult.QagNotFound
    }

    private fun isSupportedByUser(qagDetails: QagDetails, userId: String): Boolean {
        return when (qagDetails.status) {
            QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED -> false
            QagStatus.SELECTED_FOR_RESPONSE -> true
            QagStatus.MODERATED_ACCEPTED, QagStatus.OPEN -> supportQagRepository.getUserSupportedQags(userId = userId)
                .any { supportedQagId -> supportedQagId == qagDetails.id }
        }
    }

    private fun hasGivenFeedback(qagDetails: QagDetails, userId: String): Boolean {
        return when (qagDetails.status) {
            QagStatus.SELECTED_FOR_RESPONSE -> feedbackQagUseCase.getUserFeedbackQagIds(userId = userId)
                .any { userFeedbackQagId -> userFeedbackQagId == qagDetails.id }
            else -> false
        }
    }
}

sealed class QagResult {
    data class Success(val qag: QagWithUserData) : QagResult()
    object QagRejectedStatus : QagResult()
    object QagNotFound : QagResult()
}



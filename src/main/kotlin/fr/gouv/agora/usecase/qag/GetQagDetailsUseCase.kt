package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.QagWithUserData
import fr.gouv.agora.usecase.feedbackQag.FeedbackQagUseCase
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import org.springframework.stereotype.Service

@Service
class GetQagDetailsUseCase(
    private val qagDetailsAggregate: QagDetailsAggregate,
    private val supportQagUseCase: SupportQagUseCase,
    private val feedbackQagUseCase: FeedbackQagUseCase,
    private val mapper: QagDetailsMapper,
) {

    fun getQagDetails(qagId: String, userId: String): QagResult {
        return qagDetailsAggregate.getQag(qagId = qagId)?.let { qag ->
            when (qag.status) {
                QagStatus.ARCHIVED -> QagResult.QagNotFound
                QagStatus.MODERATED_REJECTED -> QagResult.QagRejectedStatus
                QagStatus.OPEN,
                QagStatus.MODERATED_ACCEPTED,
                QagStatus.SELECTED_FOR_RESPONSE ->
                    if (qag.status == QagStatus.OPEN && userId != qag.userId) {
                        QagResult.QagNotFound
                    } else {
                        val isHelpFul = isHelpFul(qagId, userId)
                        val hasGivenFeedback = isHelpFul != null

                        QagResult.Success(
                            QagWithUserData(
                                qagDetails = if (!hasGivenFeedback) {
                                    mapper.toQagWithoutFeedbackResults(qag)
                                } else qag,
                                canShare = qag.status == QagStatus.MODERATED_ACCEPTED || qag.status == QagStatus.SELECTED_FOR_RESPONSE,
                                canSupport = qag.status == QagStatus.OPEN || qag.status == QagStatus.MODERATED_ACCEPTED,
                                canDelete = qag.userId == userId && qag.status != QagStatus.SELECTED_FOR_RESPONSE,
                                isAuthor = qag.userId == userId,
                                isSupportedByUser = isSupportedByUser(qagDetails = qag, userId = userId),
                                isHelpful = isHelpFul
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
            QagStatus.MODERATED_ACCEPTED, QagStatus.OPEN -> supportQagUseCase.getUserSupportedQagIds(userId = userId)
                .any { supportedQagId -> supportedQagId == qagDetails.id }
        }
    }

    private fun isHelpFul(qagId: String, userId: String): Boolean? {
        return feedbackQagUseCase.getFeedbackForQagAndUser(qagId, userId)
    }
}

sealed class QagResult {
    data class Success(val qag: QagWithUserData) : QagResult()
    object QagRejectedStatus : QagResult()
    object QagNotFound : QagResult()
}



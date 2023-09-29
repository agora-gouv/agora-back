package fr.social.gouv.agora.usecase.qagModerating

import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.notification.SendQagNotificationUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Service

@Service
class PutQagModeratingUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val sendQagNotificationUseCase: SendQagNotificationUseCase,
    private val qagUpdatesRepository: QagUpdatesRepository,
) {
    fun putModeratingQagStatus(qagId: String, qagModeratingStatus: Boolean, userId: String): ModeratingQagResult {
        return when (qagInfoRepository.getQagInfo(qagId = qagId)?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_ACCEPTED, QagStatus.MODERATED_REJECTED, QagStatus.SELECTED_FOR_RESPONSE -> ModeratingQagResult.FAILURE
            QagStatus.OPEN -> updateStatusAndSendNotificationIfRequired(
                qagId = qagId,
                qagModeratingStatus = qagModeratingStatus,
                userId = userId,
            )
        }
    }

    private fun updateStatusAndSendNotificationIfRequired(
        qagId: String,
        qagModeratingStatus: Boolean,
        userId: String,
    ): ModeratingQagResult {
        val updateQagStatus = qagInfoRepository.updateQagStatus(
            qagId = qagId,
            newQagStatus = toQagStatus(qagModeratingStatus),
        )
        return when (updateQagStatus) {
            QagUpdateResult.Failure -> ModeratingQagResult.FAILURE
            is QagUpdateResult.Success -> {
                qagUpdatesRepository.insertQagUpdates(
                    QagInsertingUpdates(
                        qagId = qagId,
                        newQagStatus = toQagStatus(qagModeratingStatus),
                        userId = userId,
                        reason = null,
                        shouldDelete = false,
                    )
                )
                if (qagModeratingStatus) {
                    sendQagNotificationUseCase.sendNotificationQagAccepted(qagId = qagId)
                } else {
                    sendQagNotificationUseCase.sendNotificationQagRejected(qagId = qagId)
                }
                ModeratingQagResult.SUCCESS
            }
        }
    }

    private fun toQagStatus(qagModeratingStatus: Boolean): QagStatus {
        return when (qagModeratingStatus) {
            true -> QagStatus.MODERATED_ACCEPTED
            false -> QagStatus.MODERATED_REJECTED
        }
    }
}

enum class ModeratingQagResult {
    SUCCESS, FAILURE
}
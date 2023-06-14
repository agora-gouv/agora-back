package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import org.springframework.stereotype.Service

@Service
class PutQagModeratingUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val sendNotificationQagModeratedUseCase: SendNotificationQagModeratedUseCase,
) {
    fun putModeratingQagStatus(qagId: String, qagModeratingStatus: Boolean): ModeratingQagResult {
        return when (qagInfoRepository.getQagInfo(qagId = qagId)?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_ACCEPTED, QagStatus.MODERATED_REJECTED, QagStatus.SELECTED_FOR_RESPONSE -> ModeratingQagResult.FAILURE
            QagStatus.OPEN -> updateStatusAndSendNotificationIfRequired(
                qagId = qagId,
                qagModeratingStatus = qagModeratingStatus,
            )
        }
    }

    private fun updateStatusAndSendNotificationIfRequired(
        qagId: String,
        qagModeratingStatus: Boolean,
    ): ModeratingQagResult {
        val updateQagStatus = qagInfoRepository.updateQagStatus(
            qagId = qagId,
            newQagStatus = toQagStatus(qagModeratingStatus),
        )
        return when (updateQagStatus) {
            QagUpdateResult.FAILURE -> ModeratingQagResult.FAILURE
            QagUpdateResult.SUCCESS -> {
                if (!qagModeratingStatus) {
                    sendNotificationQagModeratedUseCase.sendNotificationQagModeratedMessage(qagId = qagId)
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
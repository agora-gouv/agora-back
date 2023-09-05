package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQagModerateResult
import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Service

@Service
class ModerateModeratusQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val sendNotificationdUseCase: SendNotificationQagModeratedUseCase,
    private val qagUpdatesRepository: QagUpdatesRepository,
) {

    companion object {
        private val VALID_QAG_STATUS_FOR_MODERATION = listOf(
            QagStatus.OPEN,
            QagStatus.MODERATED_ACCEPTED,
            QagStatus.MODERATED_REJECTED,
        )
    }

    fun moderateQag(qagId: String, isAccepted: Boolean, userId: String): ModeratusQagModerateResult {
        return qagInfoRepository.getQagInfo(qagId)
            ?.takeIf { qagInfo -> VALID_QAG_STATUS_FOR_MODERATION.contains(qagInfo.status) }
            ?.let { qagInfo -> changeQagStatus(qagInfo = qagInfo, isAccepted = isAccepted, userId = userId) }
            ?: ModeratusQagModerateResult.NOT_FOUND
    }

    private fun changeQagStatus(qagInfo: QagInfo, isAccepted: Boolean, userId: String): ModeratusQagModerateResult {
        val newQagStatus = when (isAccepted) {
            true -> QagStatus.MODERATED_ACCEPTED
            false -> QagStatus.MODERATED_REJECTED
        }

        val updateQagStatus = if (qagInfo.status != newQagStatus) {
            qagInfoRepository.updateQagStatus(qagId = qagInfo.id, newQagStatus = newQagStatus)
        } else QagUpdateResult.SUCCESS

        return when (updateQagStatus) {
            QagUpdateResult.SUCCESS -> {
                notifyUpdateIfRequired(qagInfo = qagInfo, isAccepted = isAccepted)
                qagUpdatesRepository.insertQagUpdates(
                    QagInsertingUpdates(qagId = qagInfo.id, newQagStatus = newQagStatus, userId = userId)
                )
                ModeratusQagModerateResult.SUCCESS
            }
            QagUpdateResult.FAILURE -> ModeratusQagModerateResult.FAILURE
        }
    }

    private fun notifyUpdateIfRequired(qagInfo: QagInfo, isAccepted: Boolean) {
        when (qagInfo.status) {
            QagStatus.OPEN -> if (isAccepted) {
                sendNotificationdUseCase.sendNotificationQagAccepted(qagInfo.id)
            } else {
                sendNotificationdUseCase.sendNotificationQagRejected(qagInfo.id)
            }
            QagStatus.MODERATED_ACCEPTED -> if (!isAccepted) {
                sendNotificationdUseCase.sendNotificationQagRejected(qagInfo.id)
            }
            QagStatus.MODERATED_REJECTED -> if (isAccepted) {
                sendNotificationdUseCase.sendNotificationQagAcceptedAfterReject(qagInfo.id)
            }
            QagStatus.ARCHIVED, QagStatus.SELECTED_FOR_RESPONSE -> throw IllegalStateException("Can not moderate qag with status ${qagInfo.status}")
        }
    }

}
package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQagModerateResult
import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.social.gouv.agora.usecase.notification.SendQagNotificationUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.social.gouv.agora.usecase.qagPreview.repository.QagPreviewPageRepository
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Service

@Service
class ModerateModeratusQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val sendNotificationdUseCase: SendQagNotificationUseCase,
    private val qagUpdatesRepository: QagUpdatesRepository,
    private val moderatusQagLockRepository: ModeratusQagLockRepository,
    private val previewPageRepository: QagPreviewPageRepository,
) {

    companion object {
        private val VALID_QAG_STATUS_FOR_MODERATION = listOf(
            QagStatus.OPEN,
            QagStatus.MODERATED_ACCEPTED,
            QagStatus.MODERATED_REJECTED,
        )
    }

    fun moderateQag(moderateQagOptions: ModerateQagOptions): ModeratusQagModerateResult {
        return qagInfoRepository.getQagInfo(moderateQagOptions.qagId)
            ?.takeIf { qagInfo -> VALID_QAG_STATUS_FOR_MODERATION.contains(qagInfo.status) }
            ?.let { qagInfo -> changeQagStatus(qagInfo = qagInfo, moderateQagOptions = moderateQagOptions) }
            ?: ModeratusQagModerateResult.NOT_FOUND
    }

    private fun changeQagStatus(qagInfo: QagInfo, moderateQagOptions: ModerateQagOptions): ModeratusQagModerateResult {
        val newQagStatus = when (moderateQagOptions.isAccepted) {
            true -> QagStatus.MODERATED_ACCEPTED
            false -> QagStatus.MODERATED_REJECTED
        }

        val updateQagStatus = if (qagInfo.status != newQagStatus) {
            qagInfoRepository.updateQagStatus(qagId = qagInfo.id, newQagStatus = newQagStatus)
        } else QagUpdateResult.SUCCESS

        return when (updateQagStatus) {
            QagUpdateResult.SUCCESS -> {
                notifyUpdateIfRequired(qagInfo = qagInfo, isAccepted = moderateQagOptions.isAccepted)
                qagUpdatesRepository.insertQagUpdates(
                    QagInsertingUpdates(
                        qagId = qagInfo.id,
                        newQagStatus = newQagStatus,
                        userId = moderateQagOptions.userId,
                        reason = moderateQagOptions.reason,
                        shouldDelete = moderateQagOptions.shouldDelete,
                    )
                )
                ModeratusQagModerateResult.SUCCESS
            }
            QagUpdateResult.FAILURE -> ModeratusQagModerateResult.FAILURE
        }
    }

    private fun notifyUpdateIfRequired(qagInfo: QagInfo, isAccepted: Boolean) {
        when (qagInfo.status) {
            QagStatus.OPEN -> {
                if (isAccepted) {
                    sendNotificationdUseCase.sendNotificationQagAccepted(qagInfo.id)
                    previewPageRepository.getQagLatestList(thematiqueId = null)
                    previewPageRepository.getQagLatestList(thematiqueId = qagInfo.thematiqueId)
                } else {
                    sendNotificationdUseCase.sendNotificationQagRejected(qagInfo.id)
                }
                moderatusQagLockRepository.removeLockedQagId(qagInfo.id)
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

data class ModerateQagOptions(
    val qagId: String,
    val userId: String,
    val isAccepted: Boolean,
    val reason: String?,
    val shouldDelete: Boolean,
)
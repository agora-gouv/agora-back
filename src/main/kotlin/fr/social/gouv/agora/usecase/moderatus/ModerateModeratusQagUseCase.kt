package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQagModerateResult
import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.social.gouv.agora.usecase.notification.SendQagNotificationUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.social.gouv.agora.usecase.qag.repository.QagWithSupportCountCacheRepository
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Service

@Service
class ModerateModeratusQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val sendNotificationUseCase: SendQagNotificationUseCase,
    private val qagUpdatesRepository: QagUpdatesRepository,
    private val moderatusQagLockRepository: ModeratusQagLockRepository,
    private val previewPageRepository: QagWithSupportCountCacheRepository,
) {

    companion object {
        private val VALID_QAG_STATUS_FOR_MODERATION = listOf(
            QagStatus.OPEN,
            QagStatus.MODERATED_ACCEPTED,
            QagStatus.MODERATED_REJECTED,
        )
        private const val MAX_PREVIEW_LIST_SIZE = 10
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
        } else QagUpdateResult.Success(qagInfo)

        return when (updateQagStatus) {
            is QagUpdateResult.Success -> {
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
            QagUpdateResult.Failure -> ModeratusQagModerateResult.FAILURE
        }
    }

    private fun notifyUpdateIfRequired(qagInfo: QagInfo, isAccepted: Boolean) {
        when (qagInfo.status) {
            QagStatus.OPEN -> {
                if (isAccepted) {
                    sendNotificationUseCase.sendNotificationQagAccepted(qagInfo.id)
                    val popularQagsNoThematique = previewPageRepository.getQagPopularList(thematiqueId = null)
                    if (popularQagsNoThematique != null && popularQagsNoThematique.size < MAX_PREVIEW_LIST_SIZE) {
                        previewPageRepository.evictQagPopularList(thematiqueId = null)
                    }
                    val popularQagsWithThematique =
                        previewPageRepository.getQagPopularList(thematiqueId = qagInfo.thematiqueId)
                    if (popularQagsWithThematique != null && popularQagsWithThematique.size < MAX_PREVIEW_LIST_SIZE) {
                        previewPageRepository.evictQagPopularList(thematiqueId = qagInfo.thematiqueId)
                    }
                    previewPageRepository.evictQagLatestList(thematiqueId = null)
                    previewPageRepository.evictQagLatestList(thematiqueId = qagInfo.thematiqueId)
                } else {
                    sendNotificationUseCase.sendNotificationQagRejected(qagInfo.id)
                }
                moderatusQagLockRepository.removeLockedQagId(qagInfo.id)
            }
            QagStatus.MODERATED_ACCEPTED -> if (!isAccepted) {
                sendNotificationUseCase.sendNotificationQagRejected(qagInfo.id)
                previewPageRepository.evictQagLatestList(thematiqueId = null)
                previewPageRepository.evictQagLatestList(thematiqueId = qagInfo.thematiqueId)
            }
            QagStatus.MODERATED_REJECTED -> if (isAccepted) {
                sendNotificationUseCase.sendNotificationQagAcceptedAfterReject(qagInfo.id)
                val popularQagsNoThematique = previewPageRepository.getQagPopularList(thematiqueId = null)
                if (popularQagsNoThematique != null && popularQagsNoThematique.size < MAX_PREVIEW_LIST_SIZE) {
                    previewPageRepository.evictQagPopularList(thematiqueId = null)
                }
                val popularQagsWithThematique =
                    previewPageRepository.getQagPopularList(thematiqueId = qagInfo.thematiqueId)
                if (popularQagsWithThematique != null && popularQagsWithThematique.size < MAX_PREVIEW_LIST_SIZE) {
                    previewPageRepository.evictQagPopularList(thematiqueId = qagInfo.thematiqueId)
                }
                previewPageRepository.evictQagLatestList(thematiqueId = null)
                previewPageRepository.evictQagLatestList(thematiqueId = qagInfo.thematiqueId)
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
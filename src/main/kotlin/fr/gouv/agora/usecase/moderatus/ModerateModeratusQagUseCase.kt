package fr.gouv.agora.usecase.moderatus

import fr.gouv.agora.domain.ModeratusQagModerateResult
import fr.gouv.agora.domain.QagInsertingUpdates
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.gouv.agora.usecase.notification.SendQagNotificationUseCase
import fr.gouv.agora.usecase.qag.repository.*
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Service

@Service
class ModerateModeratusQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val sendQagNotificationUseCase: SendQagNotificationUseCase,
    private val qagUpdatesRepository: QagUpdatesRepository,
    private val moderatusQagLockRepository: ModeratusQagLockRepository,
    private val qagPreviewCacheRepository: QagPreviewCacheRepository,
    private val qagDetailsCacheRepository: QagDetailsCacheRepository,
    private val qagListsCacheRepository: QagListsCacheRepository,
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
                    sendQagNotificationUseCase.sendNotificationQagAccepted(qagInfo.id)
                    updateCache(qagInfo)
                } else {
                    sendQagNotificationUseCase.sendNotificationQagRejected(qagInfo.id)
                }
                moderatusQagLockRepository.removeLockedQagId(qagInfo.id)
            }

            QagStatus.MODERATED_ACCEPTED -> if (!isAccepted) {
                sendQagNotificationUseCase.sendNotificationQagRejected(qagInfo.id)
                qagPreviewCacheRepository.evictQagLatestList(thematiqueId = null)
                qagPreviewCacheRepository.evictQagLatestList(thematiqueId = qagInfo.thematiqueId)
                qagDetailsCacheRepository.evictQag(qagId = qagInfo.id)
                qagListsCacheRepository.evictQagLatestList(thematiqueId = null, pageNumber = 1)
                qagListsCacheRepository.evictQagLatestList(thematiqueId = qagInfo.thematiqueId, pageNumber = 1)
                qagListsCacheRepository.evictQagTrendingList()
            }

            QagStatus.MODERATED_REJECTED -> if (isAccepted) {
                sendQagNotificationUseCase.sendNotificationQagAcceptedAfterReject(qagInfo.id)
                updateCache(qagInfo)
            }

            QagStatus.ARCHIVED, QagStatus.SELECTED_FOR_RESPONSE -> throw IllegalStateException("Can not moderate qag with status ${qagInfo.status}")
        }
    }

    private fun updateCache(qagInfo: QagInfo) {
        val popularQagsNoThematique = qagPreviewCacheRepository.getQagPopularList(thematiqueId = null)
        if (popularQagsNoThematique != null && popularQagsNoThematique.size < MAX_PREVIEW_LIST_SIZE) {
            qagPreviewCacheRepository.evictQagPopularList(thematiqueId = null)
        }
        val popularQagsWithThematique =
            qagPreviewCacheRepository.getQagPopularList(thematiqueId = qagInfo.thematiqueId)
        if (popularQagsWithThematique != null && popularQagsWithThematique.size < MAX_PREVIEW_LIST_SIZE) {
            qagPreviewCacheRepository.evictQagPopularList(thematiqueId = qagInfo.thematiqueId)
        }
        val popularQagListNoThematique = qagListsCacheRepository.getQagPopularList(thematiqueId = null, pageNumber = 1)
        if (popularQagListNoThematique != null && popularQagListNoThematique.qags.size < MAX_PREVIEW_LIST_SIZE) {
            qagListsCacheRepository.evictQagPopularList(thematiqueId = null, pageNumber = 1)
        }
        val popularQagListWithThematique =
            qagListsCacheRepository.getQagPopularList(thematiqueId = qagInfo.thematiqueId, pageNumber = 1)
        if (popularQagListWithThematique != null && popularQagListWithThematique.qags.size < MAX_PREVIEW_LIST_SIZE) {
            qagListsCacheRepository.getQagPopularList(thematiqueId = qagInfo.thematiqueId, pageNumber = 1)
        }
        qagListsCacheRepository.evictQagPopularList(thematiqueId = qagInfo.thematiqueId, pageNumber = 1)
        qagPreviewCacheRepository.evictQagLatestList(thematiqueId = null)
        qagPreviewCacheRepository.evictQagLatestList(thematiqueId = qagInfo.thematiqueId)
        qagDetailsCacheRepository.evictQag(qagId = qagInfo.id)
        qagListsCacheRepository.evictQagLatestList(thematiqueId = null, pageNumber = 1)
        qagListsCacheRepository.evictQagLatestList(thematiqueId = qagInfo.thematiqueId, pageNumber = 1)
        qagListsCacheRepository.evictQagTrendingList()
    }

}

data class ModerateQagOptions(
    val qagId: String,
    val userId: String,
    val isAccepted: Boolean,
    val reason: String?,
    val shouldDelete: Boolean,
)
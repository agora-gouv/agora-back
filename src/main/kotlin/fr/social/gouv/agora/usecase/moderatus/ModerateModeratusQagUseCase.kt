package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQagModerateResult
import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
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
            ?.let { changeQagStatus(qagId = qagId, isAccepted = isAccepted, userId = userId) }
            ?: ModeratusQagModerateResult.NOT_FOUND
    }

    private fun changeQagStatus(qagId: String, isAccepted: Boolean, userId: String): ModeratusQagModerateResult {
        val newQagStatus = when (isAccepted) {
            true -> QagStatus.MODERATED_ACCEPTED
            false -> QagStatus.MODERATED_REJECTED
        }

        return when (qagInfoRepository.updateQagStatus(qagId, newQagStatus)) {
            QagUpdateResult.SUCCESS -> {
                if (isAccepted) {
                    sendNotificationdUseCase.sendNotificationQagAccepted(qagId)
                } else {
                    sendNotificationdUseCase.sendNotificationQagRejected(qagId)
                }
                qagUpdatesRepository.insertQagUpdates(
                    QagInsertingUpdates(qagId = qagId, newQagStatus = newQagStatus, userId = userId)
                )
                ModeratusQagModerateResult.SUCCESS
            }
            QagUpdateResult.FAILURE -> ModeratusQagModerateResult.FAILURE
        }
    }

}
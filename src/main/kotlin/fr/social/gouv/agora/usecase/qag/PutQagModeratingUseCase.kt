package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
import fr.social.gouv.agora.usecase.qag.repository.ModeratingQagResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class PutQagModeratingUseCase(
    private val qagModeratingListRepository: QagInfoRepository,
    private val sendNotificationQagModeratedUseCase: SendNotificationQagModeratedUseCase,
) {
    fun putModeratingQagStatus(qagId: String, qagModeratingStatus: Boolean): ModeratingQagResult {
        return if (qagModeratingStatus)
            qagModeratingListRepository.updateQagStatus(qagId, QagStatus.MODERATED_ACCEPTED)
        else {
            val moderatingQagResult = qagModeratingListRepository.updateQagStatus(qagId, QagStatus.MODERATED_REJECTED)
            if (moderatingQagResult == ModeratingQagResult.SUCCESS)
                sendNotificationQagModeratedUseCase.sendNotificationQagModeratedMessage(qagId = qagId)
            moderatingQagResult
        }
    }
}

package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.repository.ModeratingQagResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class PutQagModeratingUseCase(
    private val qagModeratingListRepository: QagInfoRepository,
) {
    fun putModeratingQagStatus(qagId: String, qagModeratingStatus: Boolean): ModeratingQagResult {
        return if (qagModeratingStatus)
            qagModeratingListRepository.updateQagStatus(qagId, QagStatus.MODERATED_ACCEPTED)
        else
            qagModeratingListRepository.updateQagStatus(qagId, QagStatus.MODERATED_REJECTED)
    }
}

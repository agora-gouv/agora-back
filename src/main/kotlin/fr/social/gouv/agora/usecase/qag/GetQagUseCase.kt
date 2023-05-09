package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val qagSupportQagRepository: GetSupportQagRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val getFeedbackQagRepository: GetFeedbackQagRepository,
    private val thematiqueRepository: ThematiqueRepository,
) {
    fun getQag(qagId: String, deviceId: String): Qag? {
        // TODO transform deviceId to userId
        return qagInfoRepository.getQagInfo(qagId)?.let { qagInfo ->
            thematiqueRepository.getThematique(qagInfo.thematiqueId)?.let { thematique ->
                Qag(
                    id = qagInfo.id,
                    thematique = thematique,
                    title = qagInfo.title,
                    description = qagInfo.description,
                    date = qagInfo.date,
                    status = qagInfo.status,
                    username = qagInfo.username,
                    support = qagSupportQagRepository.getSupportQag(qagId = qagId, userId = deviceId),
                    response = responseQagRepository.getResponseQag(qagId = qagId),
                    feedback = getFeedbackQagRepository.getFeedbackQagStatus(qagId = qagId, userId = deviceId)
                )
            }
        }
    }

}


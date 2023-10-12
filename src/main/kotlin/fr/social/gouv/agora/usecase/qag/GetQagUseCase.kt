package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.domain.QagStatus
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
    fun getQag(qagId: String, userId: String): QagResult {
        val qag = qagInfoRepository.getQagInfo(qagId)?.let { qagInfo ->
            thematiqueRepository.getThematique(qagInfo.thematiqueId)?.let { thematique ->
                qagSupportQagRepository.getSupportQag(qagId = qagId, userId = userId)?.let { supportQag ->
                    Qag(
                        id = qagInfo.id,
                        thematique = thematique,
                        title = qagInfo.title,
                        description = qagInfo.description,
                        date = qagInfo.date,
                        status = qagInfo.status,
                        username = qagInfo.username,
                        userId = qagInfo.userId,
                        support = supportQag,
                        response = responseQagRepository.getResponseQag(qagId = qagId),
                        feedback = getFeedbackQagRepository.getFeedbackQagStatus(qagId = qagId, userId = userId),
                        feedbackResults = getFeedbackQagRepository.getFeedbackQagList(qagId = qagId),
                    )
                }
            }
        }
        return when (qag?.status) {
            null, QagStatus.ARCHIVED -> QagResult.QagNotFound
            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED, QagStatus.SELECTED_FOR_RESPONSE -> QagResult.Success(qag)
            QagStatus.MODERATED_REJECTED -> QagResult.QagRejectedStatus
        }
    }
}

sealed class QagResult {
    data class Success(val qag: Qag) : QagResult()
    object QagRejectedStatus : QagResult()
    object QagNotFound : QagResult()
}



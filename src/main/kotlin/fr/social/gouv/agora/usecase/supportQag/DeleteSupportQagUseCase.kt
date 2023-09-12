package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class DeleteSupportQagUseCase(
    private val responseQagRepository: ResponseQagRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
) {
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        if (responseQagRepository.getResponseQag(supportQagDeleting.qagId) != null) {
            return SupportQagResult.FAILURE
        }
        if (getSupportQagRepository.getSupportQag(
                qagId = supportQagDeleting.qagId,
                userId = supportQagDeleting.userId,
            )?.isSupportedByUser == false
        ) return SupportQagResult.FAILURE

        return when (qagInfoRepository.getQagInfo(supportQagDeleting.qagId)?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED, QagStatus.SELECTED_FOR_RESPONSE -> SupportQagResult.FAILURE
            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED -> supportQagRepository.deleteSupportQag(
                SupportQagDeleting(
                    qagId = supportQagDeleting.qagId,
                    userId = supportQagDeleting.userId,
                )
            )
        }
    }
}
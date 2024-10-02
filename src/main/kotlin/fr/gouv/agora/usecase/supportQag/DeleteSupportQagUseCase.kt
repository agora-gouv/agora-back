package fr.gouv.agora.usecase.supportQag

import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.SupportQagDeleting
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeleteSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(DeleteSupportQagUseCase::class.java)

    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        if (!getSupportQagRepository.isQagSupported(
                qagId = supportQagDeleting.qagId,
                userId = supportQagDeleting.userId,
            )
        ) {
            logger.debug("⚠️ Remove support error: already unsupported")
            return SupportQagResult.FAILURE
        }

        val qagInfo = qagInfoRepository.getQagInfo(supportQagDeleting.qagId)
        return when (qagInfo?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED, QagStatus.SELECTED_FOR_RESPONSE -> {
                logger.error("⚠️ Remove support error: QaG with invalid status")
                SupportQagResult.FAILURE
            }

            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED -> {
                return supportQagRepository.deleteSupportQag(supportQagDeleting)
            }
        }
    }
}

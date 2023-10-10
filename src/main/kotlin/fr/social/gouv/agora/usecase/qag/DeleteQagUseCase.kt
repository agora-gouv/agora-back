package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagDeleteLog
import fr.social.gouv.agora.usecase.qag.repository.QagDeleteLogRepository
import fr.social.gouv.agora.usecase.qag.repository.QagDeleteResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagPreview.repository.QagPreviewPageRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.springframework.stereotype.Service

@Service
class DeleteQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val qagPreviewPageRepository: QagPreviewPageRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagDeleteLogRepository: QagDeleteLogRepository,
) {
    fun deleteQagById(userId: String, qagId: String): QagDeleteResult {
        val qagInfo = qagInfoRepository.getQagInfo(qagId = qagId)
        return when (qagInfo?.userId) {
            userId -> {
                qagInfoRepository.deleteQag(qagId = qagId)
                supportQagRepository.deleteSupportListByQagId(qagId = qagId)
                qagDeleteLogRepository.insertQagDeleteLog(qagDeleteLog = QagDeleteLog(userId = userId, qagId = qagId))
                qagPreviewPageRepository.evictQagSupportedList(userId = userId, thematiqueId = null)
                qagPreviewPageRepository.evictQagSupportedList(
                    userId = userId,
                    thematiqueId = qagInfo.thematiqueId,
                )
                QagDeleteResult.SUCCESS
            }

            else -> {
                QagDeleteResult.FAILURE
            }
        }
    }
}
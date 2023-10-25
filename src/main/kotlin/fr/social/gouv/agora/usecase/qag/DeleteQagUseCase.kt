package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagDeleteLog
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.repository.*
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.springframework.stereotype.Service

@Service
class DeleteQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagDeleteLogRepository: QagDeleteLogRepository,
    private val qagPreviewCacheRepository: QagPreviewCacheRepository,
    private val qagDetailsCacheRepository: QagDetailsCacheRepository,
    private val askQagStatusCacheRepository: AskQagStatusCacheRepository,
) {
    fun deleteQagById(userId: String, qagId: String): QagDeleteResult {
        val qagInfo = qagInfoRepository.getQagInfo(qagId = qagId)
        return when {
            qagInfo?.userId == userId && qagInfo.status != QagStatus.SELECTED_FOR_RESPONSE -> {
                val deleteResult = qagInfoRepository.deleteQag(qagId = qagId)
                when (deleteResult) {
                    is QagDeleteResult.Success -> {
                        supportQagRepository.deleteSupportListByQagId(qagId = qagId)
                        qagDeleteLogRepository.insertQagDeleteLog(
                            qagDeleteLog = QagDeleteLog(
                                userId = userId,
                                qagId = qagId,
                            )
                        )
                        qagPreviewCacheRepository.evictQagSupportedList(userId = userId, thematiqueId = null)
                        qagPreviewCacheRepository.evictQagSupportedList(
                            userId = userId,
                            thematiqueId = qagInfo.thematiqueId,
                        )
                        qagDetailsCacheRepository.evictQag(qagId = qagId)
                        askQagStatusCacheRepository.evictAskQagStatus(userId = userId)
                    }
                    QagDeleteResult.Failure -> {} // Do nothing
                }
                return deleteResult
            }
            else -> QagDeleteResult.Failure
        }
    }
}
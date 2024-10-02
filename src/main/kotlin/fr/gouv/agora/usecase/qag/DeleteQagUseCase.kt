package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDeleteLog
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.qag.repository.QagDeleteLogRepository
import fr.gouv.agora.usecase.qag.repository.QagDeleteResult
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.springframework.stereotype.Service

@Service
class DeleteQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagDeleteLogRepository: QagDeleteLogRepository,
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
                    }

                    QagDeleteResult.Failure -> {} // Do nothing
                }
                return deleteResult
            }

            else -> QagDeleteResult.Failure
        }
    }
}

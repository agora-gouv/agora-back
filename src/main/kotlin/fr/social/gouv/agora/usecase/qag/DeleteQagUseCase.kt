package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.usecase.qag.repository.QagDeleteResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.springframework.stereotype.Service

@Service
class DeleteQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
) {
    fun deleteQagById(userId: String, qagId: String): QagDeleteResult {
        val qagUserId = qagInfoRepository.getQagInfo(qagId = qagId)?.userId
        return when {
            (qagUserId == userId) -> {
                qagInfoRepository.deleteQag(qagId = qagId)
                supportQagRepository.deleteSupportListByQagId(qagId = qagId)
                QagDeleteResult.SUCCESS
            }
            else -> {
                QagDeleteResult.FAILURE
            }
        }
    }
}
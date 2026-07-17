package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagUpdateResult
import org.springframework.stereotype.Service

@Service
class AdminUpdateQagStatusUseCase(
    private val qagInfoRepository: QagInfoRepository,
) {

    fun updateQagStatus(qagId: String, newStatus: QagStatus): AdminUpdateQagStatusResult {
        val qagInfo = qagInfoRepository.getQagInfo(qagId)
            ?: return AdminUpdateQagStatusResult.NotFound

        return when (qagInfoRepository.updateQagStatus(qagId = qagInfo.id, newQagStatus = newStatus)) {
            is QagUpdateResult.Success -> AdminUpdateQagStatusResult.Success
            QagUpdateResult.Failure -> AdminUpdateQagStatusResult.Failure
        }
    }
}

sealed class AdminUpdateQagStatusResult {
    object Success : AdminUpdateQagStatusResult()
    object NotFound : AdminUpdateQagStatusResult()
    object Failure : AdminUpdateQagStatusResult()
}

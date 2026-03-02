package fr.gouv.agora.usecase.qagSelection

import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagUpdateResult
import org.springframework.stereotype.Service

@Service
class UpdateWinningQagsUseCase(
    private val qagInfoRepository: QagInfoRepository,
) {
    fun execute(qagIds: List<String>): List<QagUpdateResult> {
        return qagIds.map { qagId ->
            qagInfoRepository.selectQagForResponse(qagId)
        }
    }
}

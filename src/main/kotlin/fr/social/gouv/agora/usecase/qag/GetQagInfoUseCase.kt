package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class GetQagInfoUseCase(
    private val qagInfoRepository: QagInfoRepository,
) {
    fun getQagStatus(qagId: String): QagStatus? {
        return qagInfoRepository.getQagInfo(qagId)?.status
    }
}

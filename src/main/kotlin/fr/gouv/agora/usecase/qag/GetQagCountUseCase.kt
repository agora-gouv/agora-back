package fr.gouv.agora.usecase.qag

import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class GetQagCountUseCase(val qagInfoRepository: QagInfoRepository) {
    fun execute(): Int {
        return qagInfoRepository.getQagsCount(null)
    }
}

package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.stereotype.Service

@Service
class InsertQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
) {
    fun insertQag(qagInfo: QagInfo): QagInsertionResult {
        return qagInfoRepository.insertQagInfo(qagInfo)
    }

}


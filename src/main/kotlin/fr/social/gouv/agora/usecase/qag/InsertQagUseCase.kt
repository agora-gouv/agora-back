package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.stereotype.Service

@Service
class InsertQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
) {
    fun insertQag(qagInserting: QagInserting): QagInsertionResult {
        return qagInfoRepository.insertQagInfo(qagInserting)
    }

}


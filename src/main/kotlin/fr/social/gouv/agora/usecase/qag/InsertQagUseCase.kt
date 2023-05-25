package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import fr.social.gouv.agora.usecase.qag.repository.QagLatestListRepository
import fr.social.gouv.agora.usecase.qag.repository.QagSupportedListRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.springframework.stereotype.Service

@Service
class InsertQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val qagLatestListRepository: QagLatestListRepository,
    private val qagSupportedListRepository: QagSupportedListRepository,
    private val supportQagRepository: SupportQagRepository,
) {
    fun insertQag(qagInserting: QagInserting): QagInsertionResult {
        return when (val qagInsertionResult = qagInfoRepository.insertQagInfo(qagInserting)) {
            QagInsertionResult.Failure -> QagInsertionResult.Failure
            is QagInsertionResult.Success -> {
                qagLatestListRepository.deleteQagLatestList(qagInserting.thematiqueId)
                qagSupportedListRepository.deleteQagSupportedList(
                    thematiqueId = qagInserting.thematiqueId,
                    userId = qagInserting.userId
                )
                supportQagRepository.insertSupportQag(
                    SupportQagInserting(
                        qagId = qagInsertionResult.qagId.toString(),
                        userId = qagInserting.userId
                    )
                )
                QagInsertionResult.Success(qagInsertionResult.qagId)
            }
        }
    }
}


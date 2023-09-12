package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.springframework.stereotype.Service

@Service
class InsertQagUseCase(
    private val contentSanitizer: ContentSanitizer,
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
) {

    companion object {
        private const val TITLE_MAX_LENGTH = 200
        private const val DESCRIPTION_MAX_LENGTH = 400
        private const val USERNAME_MAX_LENGTH = 50
    }

    fun insertQag(qagInserting: QagInserting): QagInsertionResult {
        if (isContentNotSaint(qagInserting)) {
            return QagInsertionResult.Failure
        }

        val qagInsertionResult = qagInfoRepository.insertQagInfo(qagInserting)
        if (qagInsertionResult is QagInsertionResult.Success) {
            supportQagRepository.insertSupportQag(
                SupportQagInserting(
                    qagId = qagInsertionResult.qagId.toString(),
                    userId = qagInserting.userId,
                )
            )
        }
        return qagInsertionResult
    }

    private fun isContentNotSaint(qagInserting: QagInserting) =
        !contentSanitizer.isContentSaint(qagInserting.title, TITLE_MAX_LENGTH)
                || !contentSanitizer.isContentSaint(qagInserting.description, DESCRIPTION_MAX_LENGTH)
                || !contentSanitizer.isContentSaint(qagInserting.username, USERNAME_MAX_LENGTH)
}


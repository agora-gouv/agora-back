package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagInserting
import fr.gouv.agora.domain.SupportQagInserting
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInsertionResult
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InsertQagUseCase(
    private val contentSanitizer: ContentSanitizer,
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(InsertQagUseCase::class.java)

    companion object {
        private const val TITLE_MAX_LENGTH = 200
        private const val DESCRIPTION_MAX_LENGTH = 400
        private const val USERNAME_MAX_LENGTH = 50
    }

    fun insertQag(qagInserting: QagInserting): QagInsertionResult {
        val qagInsertionResult = qagInfoRepository.insertQagInfo(
            qagInserting.copy(
                title = contentSanitizer.sanitize(qagInserting.title, TITLE_MAX_LENGTH),
                description = contentSanitizer.sanitize(qagInserting.description, DESCRIPTION_MAX_LENGTH),
                username = contentSanitizer.sanitize(qagInserting.username, USERNAME_MAX_LENGTH),
            )
        )
        if (qagInsertionResult is QagInsertionResult.Success) {
            supportQagRepository.insertSupportQag(
                SupportQagInserting(
                    qagId = qagInsertionResult.qagInfo.id,
                    userId = qagInserting.userId,
                )
            )
        } else {
            logger.error("⚠️ Insert QaG error")
        }
        return qagInsertionResult
    }

}

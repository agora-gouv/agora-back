package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagInserting
import fr.gouv.agora.domain.SupportQagInserting
import fr.gouv.agora.usecase.qag.repository.AskQagStatusCacheRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInsertionResult
import fr.gouv.agora.usecase.qag.repository.QagPreviewCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagCacheRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InsertQagUseCase(
    private val contentSanitizer: ContentSanitizer,
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagPreviewCacheRepository: QagPreviewCacheRepository,
    private val qagListsCacheRepository: QagListsCacheRepository,
    private val askQagStatusCacheRepository: AskQagStatusCacheRepository,
    private val supportQagCacheRepository: SupportQagCacheRepository,
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
            supportQagCacheRepository.addSupportedQagIds(
                userId = qagInserting.userId,
                qagId = qagInsertionResult.qagInfo.id,
            )
            qagPreviewCacheRepository.evictQagSupportedList(userId = qagInserting.userId, thematiqueId = null)
            qagPreviewCacheRepository.evictQagSupportedList(
                userId = qagInserting.userId,
                thematiqueId = qagInserting.thematiqueId,
            )
            qagListsCacheRepository.evictQagSupportedList(
                userId = qagInserting.userId,
                thematiqueId = null,
                pageNumber = 1
            )
            qagListsCacheRepository.evictQagSupportedList(
                userId = qagInserting.userId,
                thematiqueId = qagInserting.thematiqueId,
                pageNumber = 1
            )
            askQagStatusCacheRepository.evictAskQagStatus(userId = qagInserting.userId)
        } else {
            logger.error("⚠️ Insert QaG error")
        }
        return qagInsertionResult
    }

}

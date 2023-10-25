package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.qag.repository.AskQagStatusCacheRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import fr.social.gouv.agora.usecase.qag.repository.QagPreviewCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.springframework.stereotype.Service

@Service
class InsertQagUseCase(
    private val contentSanitizer: ContentSanitizer,
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagPreviewCacheRepository: QagPreviewCacheRepository,
    private val askQagStatusCacheRepository: AskQagStatusCacheRepository,
) {

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
            qagPreviewCacheRepository.evictQagSupportedList(userId = qagInserting.userId, thematiqueId = null)
            qagPreviewCacheRepository.evictQagSupportedList(
                userId = qagInserting.userId,
                thematiqueId = qagInserting.thematiqueId,
            )
            askQagStatusCacheRepository.evictAskQagStatus(userId = qagInserting.userId)
        } else {
            println("⚠️ Insert QaG error")
        }
        return qagInsertionResult
    }

}


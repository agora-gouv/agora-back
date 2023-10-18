package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: GetSupportQagRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val qagCacheRepository: QagCacheRepository,
    private val feedbackQagRepository: GetFeedbackQagRepository,
    private val thematiqueRepository: ThematiqueRepository,
) {
    fun getQag(qagId: String): QagResult {
        val qag = when (val cacheResult = qagCacheRepository.getQag(qagId)) {
            is QagCacheResult.CachedQag -> cacheResult.qag
            QagCacheResult.QagNotFount -> null
            QagCacheResult.QagCacheNotInitialized -> buildQag(qagId)
        }

        return when (qag?.status) {
            null, QagStatus.ARCHIVED -> QagResult.QagNotFound
            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED, QagStatus.SELECTED_FOR_RESPONSE -> QagResult.Success(qag)
            QagStatus.MODERATED_REJECTED -> QagResult.QagRejectedStatus
        }
    }

    private fun buildQag(qagId: String): Qag? {
        return qagInfoRepository.getQagWithSupportCount(qagId)?.let { qagInfo ->
            thematiqueRepository.getThematique(qagInfo.thematiqueId)?.let { thematique ->
                Qag(
                    id = qagInfo.id,
                    thematique = thematique,
                    title = qagInfo.title,
                    description = qagInfo.description,
                    date = qagInfo.date,
                    status = qagInfo.status,
                    username = qagInfo.username,
                    userId = qagInfo.userId,
                    supportCount = supportQagRepository.getQagSupportCounts(listOf(qagId))[qagId] ?: 0,
                    response = responseQagRepository.getResponseQag(qagId = qagId),
                    feedback =
                )
            }
        }.also { qag ->
            when (qag) {
                null -> qagCacheRepository.initQagNotFound(qagId)
                else -> qagCacheRepository.initQag(qag)
            }
        }
    }
}

sealed class QagResult {
    data class Success(val qag: Qag) : QagResult()
    object QagRejectedStatus : QagResult()
    object QagNotFound : QagResult()
}



package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.feedbackQag.FeedbackQagUseCase
import fr.gouv.agora.usecase.qag.repository.QagDetailsCacheRepository
import fr.gouv.agora.usecase.qag.repository.QagDetailsCacheResult
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component

@Component
class QagDetailsAggregate(
    private val qagInfoRepository: QagInfoRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val qagDetailsCacheRepository: QagDetailsCacheRepository,
    private val feedbackQagUseCase: FeedbackQagUseCase,
    private val thematiqueRepository: ThematiqueRepository,
) {

    fun getQag(qagId: String): QagDetails? {
        return when (val cacheResult = qagDetailsCacheRepository.getQag(qagId)) {
            is QagDetailsCacheResult.CachedQagDetails -> cacheResult.qagDetails
            QagDetailsCacheResult.QagDetailsNotFound -> null
            QagDetailsCacheResult.QagDetailsCacheNotInitialized -> buildQag(qagId)
        }
    }

    private fun buildQag(qagId: String): QagDetails? {
        return qagInfoRepository.getQagWithSupportCount(qagId)?.let { qagInfo ->
            thematiqueRepository.getThematique(qagInfo.thematiqueId)?.let { thematique ->
                val response = if (qagInfo.status == QagStatus.SELECTED_FOR_RESPONSE) {
                    responseQagRepository.getResponseQag(qagId = qagId)
                } else null
                val feedbackResults = if (response != null) {
                    feedbackQagUseCase.getFeedbackResults(qagId = qagId)
                } else null

                QagDetails(
                    id = qagInfo.id,
                    thematique = thematique,
                    title = qagInfo.title,
                    description = qagInfo.description,
                    date = qagInfo.date,
                    status = qagInfo.status,
                    username = qagInfo.username,
                    userId = qagInfo.userId,
                    supportCount = qagInfo.supportCount,
                    response = response,
                    feedbackResults = feedbackResults,
                )
            }
        }.also { qag ->
            when (qag) {
                null -> qagDetailsCacheRepository.initQagNotFound(qagId)
                else -> qagDetailsCacheRepository.initQag(qag)
            }
        }
    }

}
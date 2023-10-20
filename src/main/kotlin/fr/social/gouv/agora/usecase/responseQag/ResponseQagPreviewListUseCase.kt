package fr.social.gouv.agora.usecase.responseQag

import fr.social.gouv.agora.domain.IncomingResponsePreview
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagPreviewCacheRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ResponseQagPreviewListUseCase(
    private val cacheRepository: ResponseQagPreviewCacheRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ResponseQagPreviewListMapper,
) {

    fun getResponseQagPreviewList(): ResponseQagPreviewList {
        return cacheRepository.getResponseQagPreviewList() ?: buildResponseQagPreviewList().also {
            cacheRepository.initResponseQagPreviewList(it)
        }
    }

    private fun buildResponseQagPreviewList(): ResponseQagPreviewList {
        val qags = qagInfoRepository.getQagResponsesWithSupportCount()
        if (qags.isEmpty()) return ResponseQagPreviewList(incomingResponses = emptyList(), responses = emptyList())

        val thematiques = thematiqueRepository.getThematiqueList()
        val responses = responseQagRepository.getResponsesQag(qags.map { it.id })

        return ResponseQagPreviewList(
            incomingResponses = qags.mapNotNull { qag ->
                thematiques.find { thematique -> thematique.id == qag.thematiqueId }?.let { thematique ->
                    if (responses.none { responseQag -> responseQag.qagId == qag.id }) {
                        mapper.toIncomingResponsePreview(qag = qag, thematique = thematique)
                    } else null
                }
            },
            responses = qags.mapNotNull { qag ->
                thematiques.find { thematique -> thematique.id == qag.thematiqueId }?.let { thematique ->
                    responses.find { responseQag -> responseQag.qagId == qag.id }?.let { responseQag ->
                        mapper.toResponseQagPreview(qag = qag, thematique = thematique, responseQag = responseQag)
                    }
                }
            },
        )
    }

}

data class ResponseQagPreviewList(
    val incomingResponses: List<IncomingResponsePreview>,
    val responses: List<ResponseQagPreview>,
)
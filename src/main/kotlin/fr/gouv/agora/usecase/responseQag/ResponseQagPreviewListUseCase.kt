package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagPreviewCacheRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
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
        val qagsWithoutResponses = qagInfoRepository.getQagSelectedWithoutResponsesWithSupportCount()
        val qagsWithResponses = qagInfoRepository.getQagWithResponses()

        if (qagsWithoutResponses.isEmpty() && qagsWithResponses.isEmpty()) return ResponseQagPreviewList(
            incomingResponses = emptyList(),
            responses = emptyList(),
        )

        val thematiques = thematiqueRepository.getThematiqueList()
        val responses = if (qagsWithResponses.isNotEmpty()) {
            responseQagRepository.getResponsesQag(qagsWithResponses.map { it.id })
        } else emptyList()

        return ResponseQagPreviewList(
            incomingResponses = qagsWithoutResponses.mapNotNull { qag ->
                thematiques.find { thematique -> thematique.id == qag.thematiqueId }?.let { thematique ->
                    mapper.toIncomingResponsePreview(qag = qag, thematique = thematique)
                }
            },
            responses = qagsWithResponses.mapNotNull { qag ->
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
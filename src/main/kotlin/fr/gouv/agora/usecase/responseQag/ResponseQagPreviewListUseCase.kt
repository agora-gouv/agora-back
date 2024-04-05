package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.usecase.qag.repository.LowPriorityQagRepository
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
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
    private val lowPriorityQagRepository: LowPriorityQagRepository,
    private val mapper: ResponseQagPreviewListMapper,
    private val orderMapper: ResponseQagPreviewOrderMapper,
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

        val qagAndResponses = if (qagsWithResponses.isNotEmpty()) {
            val responses = responseQagRepository.getResponsesQag(qagsWithResponses.map { it.id })
            qagsWithResponses.mapNotNull { qag ->
                responses.find { response -> response.qagId == qag.id }?.let { response -> qag to response }
            }
        } else emptyList()

        val orderResult = orderMapper.buildOrderResult(
            lowPriorityQagIds = (qagsWithoutResponses.map { it.id } + qagsWithResponses.map { it.id })
                .takeIf { it.isNotEmpty() }
                ?.let(lowPriorityQagRepository::getLowPriorityQagIds) ?: emptyList(),
            incomingResponses = qagsWithoutResponses,
            responses = qagAndResponses,
        )

        val thematiques = thematiqueRepository.getThematiqueList()
        return ResponseQagPreviewList(
            incomingResponses = orderResult.incomingResponses.mapNotNull { qagWithOrder ->
                thematiques.find { thematique -> thematique.id == qagWithOrder.qagWithSupportCount.thematiqueId }
                    ?.let { thematique ->
                        mapper.toIncomingResponsePreview(
                            qagWithSupportCountAndOrder = qagWithOrder,
                            thematique = thematique,
                        )
                    }
            },
            responses = orderResult.responses.mapNotNull { qagWithOrder ->
                thematiques.find { thematique -> thematique.id == qagWithOrder.qagInfo.thematiqueId }
                    ?.let { thematique ->
                        mapper.toResponseQagPreview(
                            qagWithResponseAndOrder = qagWithOrder,
                            thematique = thematique,
                        )
                    }
            },
        )
    }

}

data class QagWithSupportCountAndOrder(
    val qagWithSupportCount: QagInfoWithSupportCount,
    val order: Int,
)

data class QagWithResponseAndOrder(
    val qagInfo: QagInfo,
    val response: ResponseQag,
    val order: Int,
)

data class ResponseQagPreviewList(
    val incomingResponses: List<IncomingResponsePreview>,
    val responses: List<ResponseQagPreview>,
)
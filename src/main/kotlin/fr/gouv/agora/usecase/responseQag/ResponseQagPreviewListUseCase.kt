package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.usecase.qag.repository.LowPriorityQagRepository
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
        val qagsSelectedForResponse = qagInfoRepository.getQagsSelectedForResponse()
        val qagsResponses = responseQagRepository.getResponsesQag(qagsSelectedForResponse.map { it.id })

        val qagAndResponseList = qagsSelectedForResponse
            .fold(emptyList<Pair<QagInfoWithSupportCount, ResponseQag?>>()) { acc, qagSelectedForResponse ->
                acc + Pair(qagSelectedForResponse, qagsResponses.find { it.qagId == qagSelectedForResponse.id })
            }

        if (qagAndResponseList.isEmpty()) return ResponseQagPreviewList(emptyList(), emptyList())

        val qagWithResponseList = qagAndResponseList.filter { it.second != null } as List<Pair<QagInfoWithSupportCount, ResponseQag>>
        val qagWithoutResponseList = qagAndResponseList.filter { it.second == null }.map { it.first }

        val orderedQags = orderMapper.buildOrderResult(
            lowPriorityQagIds = lowPriorityQagRepository.getLowPriorityQagIds(qagAndResponseList.map { it.first.id }),
            incomingResponses = qagWithoutResponseList,
            responses = qagWithResponseList,
        )

        val thematiques = thematiqueRepository.getThematiqueList()
        return ResponseQagPreviewList(
            incomingResponses = orderedQags.incomingResponses.mapNotNull { qagWithOrder ->
                thematiques.find { thematique -> thematique.id == qagWithOrder.qagWithSupportCount.thematiqueId }
                    ?.let { thematique ->
                        mapper.toIncomingResponsePreview(
                            qagWithSupportCountAndOrder = qagWithOrder,
                            thematique = thematique,
                        )
                    }
            },
            responses = orderedQags.responses.mapNotNull { qagWithOrder ->
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
    val qagInfo: QagInfoWithSupportCount,
    val response: ResponseQag,
    val order: Int,
)

data class ResponseQagPreviewList(
    val incomingResponses: List<IncomingResponsePreview>,
    val responses: List<ResponseQagPreview>,
)

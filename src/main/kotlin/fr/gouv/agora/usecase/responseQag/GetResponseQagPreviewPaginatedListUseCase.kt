package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagPreviewWithoutOrder
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class GetResponseQagPreviewPaginatedListUseCase(
    private val responseQagRepository: ResponseQagRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ResponseQagPreviewListMapper,
) {
    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getResponseQagPreviewPaginatedList(pageNumber: Int): ResponseQagPaginatedList? {
        if (pageNumber <= 0) return null

        val responsesCount = responseQagRepository.getResponsesQagCount()
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (offset > responsesCount) return null

        return ResponseQagPaginatedList(
            responsesQag = toResponseQagPreview(responseQagRepository.getResponsesQag(offset)),
            maxPageNumber = ceil(responsesCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        )
    }

    private fun toResponseQagPreview(responsesQag: List<ResponseQag>): List<ResponseQagPreviewWithoutOrder> {
        val qags = qagInfoRepository.getQagsInfo(responsesQag.map { it.qagId })
        val thematiques = thematiqueRepository.getThematiqueList()

        return responsesQag.mapNotNull { responseQag ->
            qags.find { qagInfo -> qagInfo.id == responseQag.qagId }?.let { qagInfo ->
                thematiques.find { thematique -> thematique.id == qagInfo.thematiqueId }?.let { thematique ->
                    mapper.toResponseQagPreviewWithoutOrder(
                        qagInfo = qagInfo,
                        responseQag = responseQag,
                        thematique = thematique,
                    )
                }
            }
        }
    }
}

data class ResponseQagPaginatedList(
    val responsesQag: List<ResponseQagPreviewWithoutOrder>,
    val maxPageNumber: Int,
)

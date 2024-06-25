package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagPreviewWithoutOrder
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class GetResponseQagPreviewPaginatedListUseCase(
    private val responseQagRepository: ResponseQagRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ResponseQagPreviewListMapper,
) {
    private val logger = LoggerFactory.getLogger(GetResponseQagPreviewPaginatedListUseCase::class.java)

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 5
    }

    fun getResponseQagPreviewPaginatedList(pageNumber: Int): ResponseQagPaginatedList? {
        if (pageNumber <= 0) return null

        val responsesCount = responseQagRepository.getResponsesQagCount()
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (offset > responsesCount) return null

        return ResponseQagPaginatedList(
            responsesQag = toResponseQagPreview(responseQagRepository.getResponsesQag(offset, MAX_PAGE_LIST_SIZE))
                .sortedByDescending { it.responseDate },
            maxPageNumber = ceil(responsesCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        )
    }

    private fun toResponseQagPreview(responsesQag: List<ResponseQag>): List<ResponseQagPreviewWithoutOrder> {
        val qags = qagInfoRepository.getQagsInfo(responsesQag.map { it.qagId })

        return responsesQag.mapNotNull { responseQag ->
            val qagInfo = qags.find { qagInfo -> qagInfo.id == responseQag.qagId }
            if (qagInfo == null) {
                logger.error("toResponseQagPreview - la réponse de ${responseQag.author} à la question '${responseQag.qagId}' n'a pas été trouvée")
                return@mapNotNull null
            }

            val thematique = thematiqueRepository.getThematique(qagInfo.thematiqueId)
            if (thematique == null) {
                logger.error("toResponseQagPreview - la thématique '${qagInfo.thematiqueId}' n'a pas été trouvée pour la QaG ${qagInfo.id}")
                return@mapNotNull null
            }

            return@mapNotNull mapper.toResponseQagPreviewWithoutOrder(
                qagInfo = qagInfo,
                responseQag = responseQag,
                thematique = thematique,
            )
        }
    }
}

data class ResponseQagPaginatedList(
    val responsesQag: List<ResponseQagPreviewWithoutOrder>,
    val maxPageNumber: Int,
)

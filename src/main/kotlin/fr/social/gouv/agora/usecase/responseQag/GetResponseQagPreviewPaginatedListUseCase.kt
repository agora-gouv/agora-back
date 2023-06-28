package fr.social.gouv.agora.usecase.responseQag

import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class GetResponseQagPreviewPaginatedListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val qagRepository: QagInfoRepository,
    private val responseQagRepository: ResponseQagRepository,
) {
    companion object {
        private const val MAX_PREVIEW_LIST_SIZE = 20
    }

    fun getResponseQagPreviewPaginatedList(pageNumber: Int): ResponseQagPaginatedList? {
        if (pageNumber <= 0) return null
        val listResponseQag = getResponseQagPreviewList()

        val minIndex = (pageNumber - 1) * MAX_PREVIEW_LIST_SIZE
        if (minIndex > listResponseQag.size) return null
        val maxIndex = Integer.min(pageNumber * MAX_PREVIEW_LIST_SIZE, listResponseQag.size)

        val listResponseQagPaginated = listResponseQag
            .subList(fromIndex = minIndex, toIndex = maxIndex)

        return ResponseQagPaginatedList(
            listResponseQag = listResponseQagPaginated,
            maxPageNumber = ceil(listResponseQag.size.toDouble() / MAX_PREVIEW_LIST_SIZE.toDouble()).toInt(),
        )
    }

    private fun getResponseQagPreviewList(): List<ResponseQagPreview>
    {
        return responseQagRepository.getAllResponseQag().sortedByDescending { it.responseDate }
            .mapNotNull { responseQag ->
                val qagInfo = qagRepository.getQagInfo(responseQag.qagId)
                thematiqueRepository.getThematiqueList()
                    .find { thematique -> thematique.id == qagInfo?.thematiqueId }
                    ?.let { thematique ->
                        qagInfo?.title?.let { title ->
                            ResponseQagPreview(
                                qagId = responseQag.qagId,
                                thematique = thematique,
                                title = title,
                                author = responseQag.author,
                                authorPortraitUrl = responseQag.authorPortraitUrl,
                                responseDate = responseQag.responseDate,
                            )
                        }
                    }
            }
    }

    data class ResponseQagPaginatedList(
        val listResponseQag: List<ResponseQagPreview>,
        val maxPageNumber: Int,
    )
}

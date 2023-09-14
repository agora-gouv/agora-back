package fr.social.gouv.agora.usecase.qagPaginated

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.QagFilters
import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import org.springframework.stereotype.Service
import java.lang.Integer.min
import kotlin.math.ceil

@Service
class QagPaginatedUseCase(
    private val getQagListUseCase: GetQagWithSupportAndThematiqueUseCase,
    private val filterGenerator: QagPaginatedFilterGenerator,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getPopularQagPaginated(userId: String, pageNumber: Int, thematiqueId: String?): QagsAndMaxPageCount? {
        return getQagPaginated(
            userId = userId,
            pageNumber = pageNumber,
            qagFilters = filterGenerator.getPaginatedQagFilters(
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId,
            ),
            sortByDescendingSelector = { qag -> qag.supportQagInfoList.size },
        )
    }

    fun getLatestQagPaginated(userId: String, pageNumber: Int, thematiqueId: String?): QagsAndMaxPageCount? {
        return getQagPaginated(
            userId = userId,
            pageNumber = pageNumber,
            qagFilters = filterGenerator.getPaginatedQagFilters(
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId,
            ),
            sortByDescendingSelector = { qag -> qag.qagInfo.date },
        )
    }

    fun getSupportedQagPaginated(userId: String, pageNumber: Int, thematiqueId: String?): QagsAndMaxPageCount? {
        return getQagPaginated(
            userId = userId,
            pageNumber = pageNumber,
            qagFilters = filterGenerator.getSupportedPaginatedQagFilters(
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId,
            ),
            sortByDescendingSelector = { qag ->
                val isAuthorQag = qag.qagInfo.userId == userId
                if (isAuthorQag) qag.qagInfo.date
                else qag.supportQagInfoList.find { supportQagInfo -> supportQagInfo.userId == userId }?.supportDate
            },
        )
    }

    private fun <R : Comparable<R>> getQagPaginated(
        userId: String,
        pageNumber: Int,
        qagFilters: QagFilters,
        sortByDescendingSelector: (QagInfoWithSupportAndThematique) -> R?,
    ): QagsAndMaxPageCount? {
        if (pageNumber <= 0) return null
        val qagList = getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters)

        val minIndex = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (minIndex > qagList.size) return null
        val maxIndex = min(pageNumber * MAX_PAGE_LIST_SIZE, qagList.size)

        val qags = qagList
            .sortedByDescending { qag -> sortByDescendingSelector.invoke(qag) }
            .subList(fromIndex = minIndex, toIndex = maxIndex)
            .map { qag -> mapper.toPreview(qag = qag, userId = userId) }

        return QagsAndMaxPageCount(
            qags = qags,
            maxPageCount = ceil(qagList.size.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        )
    }
}

data class QagsAndMaxPageCount(
    val qags: List<QagPreview>,
    val maxPageCount: Int,
)
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
            comparator = { qag1, qag2 -> qag2.supportQagInfoList.size.compareTo(qag1.supportQagInfoList.size) },
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
            comparator = { qag1, qag2 ->
                qag2.qagInfo.date.compareTo(qag1.qagInfo.date)
            }
        )
    }

    fun getSupportedQagPaginated(userId: String, pageNumber: Int, thematiqueId: String?): QagsAndMaxPageCount? {

        val comparator = Comparator<QagInfoWithSupportAndThematique> { qag1, qag2 ->
            val userId1 = qag1.qagInfo.userId
            val userId2 = qag2.qagInfo.userId

            when {
                userId1 == userId && userId2 == userId -> qag1.qagInfo.date.compareTo(qag2.qagInfo.date) * -1
                userId1 == userId && userId2 != userId -> -1
                userId1 != userId && userId2 == userId -> 1
                else -> {
                    val supportDate1 = qag1.supportQagInfoList.find { it.userId == userId }?.supportDate
                    val supportDate2 = qag2.supportQagInfoList.find { it.userId == userId }?.supportDate
                    if (supportDate1 != null && supportDate2 != null) {
                        supportDate1.compareTo(supportDate2) * -1
                    } else {
                        0
                    }
                }
            }
        }

        return getQagPaginated(
            userId = userId,
            pageNumber = pageNumber,
            qagFilters = filterGenerator.getSupportedPaginatedQagFilters(
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId,
            ),
            comparator = comparator
        )
    }

    private fun getQagPaginated(
        userId: String,
        pageNumber: Int,
        qagFilters: QagFilters,
        comparator: Comparator<QagInfoWithSupportAndThematique>,
    ): QagsAndMaxPageCount? {
        if (pageNumber <= 0) return null
        val qagList = getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters)

        val minIndex = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (minIndex > qagList.size) return null
        val maxIndex = min(pageNumber * MAX_PAGE_LIST_SIZE, qagList.size)

        val qags = qagList
            .sortedWith(comparator)
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
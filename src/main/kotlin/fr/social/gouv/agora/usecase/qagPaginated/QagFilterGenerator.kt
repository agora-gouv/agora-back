package fr.social.gouv.agora.usecase.qagPaginated

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.QagFilters
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component

@Component
class QagFilterGenerator {

    fun getPaginatedQagFilters(userId: String, pageNumber: Int, thematiqueId: String?): QagFilters {
        return QagFilters(
            filterQagInfo = getPaginatedQagInfoFilter(
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId
            ),
            filterSupportQagInfo = { _ -> true }, // TODO
            filterSupportQagInfoList = { true },
        )
    }

    fun getSupportedPaginatedQagFilters(userId: String, pageNumber: Int, thematiqueId: String?): QagFilters {
        return QagFilters(
            filterQagInfo = getPaginatedQagInfoFilter(
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId
            ),
            filterSupportQagInfo = { _ -> true }, // TODO
            filterSupportQagInfoList = { supportQagInfoList -> supportQagInfoList.any { it.userId == userId } },
        )
    }

    private fun getPaginatedQagInfoFilter(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?
    ): (QagInfo) -> Boolean {
        return { qagInfo ->
            hasNoFilterOrIsSameThematique(qagInfo, thematiqueId) && isOpenOrAcceptedByModerator(qagInfo)
        }
    }

    private fun hasNoFilterOrIsSameThematique(qagInfo: QagInfo, thematiqueId: String?) =
        thematiqueId == null || qagInfo.thematiqueId == thematiqueId

    private fun isOpenOrAcceptedByModerator(qagInfo: QagInfo) =
        qagInfo.status == QagStatus.OPEN || qagInfo.status == QagStatus.MODERATED_ACCEPTED

}

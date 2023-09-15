package fr.social.gouv.agora.usecase.qagPreview

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import org.springframework.stereotype.Service

@Service
class GetQagPreviewListUseCase(
    private val getQagListUseCase: GetQagWithSupportAndThematiqueUseCase,
    private val filterGenerator: QagPreviewFilterGenerator,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PREVIEW_LIST_SIZE = 10
    }

    fun getQagPreviewList(userId: String, thematiqueId: String?): QagPreviewList {
        val allQags = getQagListUseCase.getQagWithSupportAndThematique(
            filterGenerator.getPreviewQagFilters(
                userId = userId,
                thematiqueId = thematiqueId,
            )
        )
        val userQagList = allQags
            .filter { qag -> qag.qagInfo.userId == userId }
            .sortedByDescending { qag -> qag.qagInfo.date }

        val otherQagList = allQags
            .filter { qag -> qag.qagInfo.userId != userId }
            .filter { qag -> getSupportFromUser(qag = qag, userId = userId) != null }
            .sortedByDescending { qag -> getSupportFromUser(qag = qag, userId = userId)?.supportDate }

        return QagPreviewList(
            popularPreviewList = allQags
                .sortedByDescending { qag -> qag.supportQagInfoList.size }
                .take(MAX_PREVIEW_LIST_SIZE)
                .map { qag -> mapper.toPreview(qag, userId) },
            latestPreviewList = allQags
                .sortedByDescending { qag -> qag.qagInfo.date }
                .take(MAX_PREVIEW_LIST_SIZE)
                .map { qag -> mapper.toPreview(qag, userId) },
            supportedPreviewList = (userQagList + otherQagList)
                .take(MAX_PREVIEW_LIST_SIZE)
                .map { qag -> mapper.toPreview(qag, userId) },
        )
    }

    private fun getSupportFromUser(
        qag: QagInfoWithSupportAndThematique,
        userId: String,
    ) = qag.supportQagInfoList.find { supportQagInfo -> supportQagInfo.userId == userId }

}

data class QagPreviewList(
    val popularPreviewList: List<QagPreview>,
    val latestPreviewList: List<QagPreview>,
    val supportedPreviewList: List<QagPreview>,
)
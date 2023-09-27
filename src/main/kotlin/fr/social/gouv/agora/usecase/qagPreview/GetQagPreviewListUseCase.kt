package fr.social.gouv.agora.usecase.qagPreview

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import fr.social.gouv.agora.usecase.qagPreview.repository.QagPreviewPageRepository
import org.springframework.stereotype.Service

@Service
class GetQagPreviewListUseCase(
    private val getQagListUseCase: GetQagWithSupportAndThematiqueUseCase,
    private val filterGenerator: QagPreviewFilterGenerator,
    private val previewPageRepository: QagPreviewPageRepository,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PREVIEW_LIST_SIZE = 10
    }

    fun getQagPreviewList(userId: String, thematiqueId: String?): QagPreviewList {
        val popularList = previewPageRepository.getQagPopularList(thematiqueId)
        val latestList = previewPageRepository.getQagLatestList(thematiqueId)
        val supportedList = previewPageRepository.getQagSupportedList(userId, thematiqueId)

        if (popularList != null && latestList != null && supportedList != null) {
            return QagPreviewList(
                popularPreviewList = popularList.map { mapper.toPreview(it, userId) },
                latestPreviewList = latestList.map { mapper.toPreview(it, userId) },
                supportedPreviewList = supportedList.map { mapper.toPreview(it, userId) },
            )
        }

        val allQags = getQagListUseCase.getQagWithSupportAndThematique(
            filterGenerator.getPreviewQagFilters(
                userId = userId,
                thematiqueId = thematiqueId,
            )
        )

        return QagPreviewList(
            popularPreviewList = (popularList ?: buildPopularQags(allQags, thematiqueId))
                .map { mapper.toPreview(it, userId) },
            latestPreviewList = (latestList ?: buildLatestQags(allQags, thematiqueId))
                .map { qag -> mapper.toPreview(qag, userId) },
            supportedPreviewList = (supportedList ?: buildSupportedQags(allQags, userId, thematiqueId))
                .map { qag -> mapper.toPreview(qag, userId) },
        )
    }

    private fun buildPopularQags(
        allQags: List<QagInfoWithSupportAndThematique>,
        thematiqueId: String?,
    ): List<QagInfoWithSupportAndThematique> {
        return allQags
            .sortedByDescending { qag -> qag.supportQagInfoList.size }
            .take(MAX_PREVIEW_LIST_SIZE)
            .also { previewPageRepository.insertQagPopularList(thematiqueId, it) }
    }

    private fun buildLatestQags(
        allQags: List<QagInfoWithSupportAndThematique>,
        thematiqueId: String?,
    ): List<QagInfoWithSupportAndThematique> {
        return allQags
            .sortedByDescending { qag -> qag.qagInfo.date }
            .take(MAX_PREVIEW_LIST_SIZE)
            .also { previewPageRepository.insertQagLatestList(thematiqueId, it) }
    }

    private fun buildSupportedQags(
        allQags: List<QagInfoWithSupportAndThematique>,
        userId: String,
        thematiqueId: String?,
    ): List<QagInfoWithSupportAndThematique> {
        val userQagList = allQags
            .filter { qag -> qag.qagInfo.userId == userId }
            .sortedByDescending { qag -> qag.qagInfo.date }

        val otherQagList = allQags
            .filter { qag -> qag.qagInfo.userId != userId }
            .filter { qag -> getSupportFromUser(qag = qag, userId = userId) != null }
            .sortedByDescending { qag -> getSupportFromUser(qag = qag, userId = userId)?.supportDate }

        return (userQagList + otherQagList)
            .take(MAX_PREVIEW_LIST_SIZE)
            .also { previewPageRepository.insertQagSupportedList(userId, thematiqueId, it) }
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
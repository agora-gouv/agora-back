package fr.social.gouv.agora.usecase.qagPreview

import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import fr.social.gouv.agora.usecase.qag.QagWithSupportCount
import fr.social.gouv.agora.usecase.qag.QagWithSupportCountUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagPreview.repository.QagPreviewPageCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class QagPreviewListUseCaseV2(
    private val qagWithSupportCountUseCase: QagWithSupportCountUseCase,
    private val thematiqueRepository: ThematiqueRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: GetSupportQagRepository,
    private val cacheRepository: QagPreviewPageCacheRepository,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PREVIEW_LIST_SIZE = 10
    }

    fun getQagPreviewList(userId: String, thematiqueId: String?): QagPreviewList {
        val popularList = cacheRepository.getQagPopularList(thematiqueId)
        val latestList = cacheRepository.getQagLatestList(thematiqueId)
        val supportedQagList = cacheRepository.getQagSupportedList(userId, thematiqueId)
        val supportedQagIds = supportQagRepository.getUserSupportedQags(userId)

        if (popularList != null && latestList != null && supportedQagList != null) {
            return QagPreviewList(
                popularPreviewList = popularList.map { mapper.toPreview(it, supportedQagIds, userId) },
                latestPreviewList = latestList.map { mapper.toPreview(it, supportedQagIds, userId) },
                supportedPreviewList = supportedQagList.map { mapper.toPreview(it, supportedQagIds, userId) },
            )
        }

        val qagList = qagWithSupportCountUseCase.getDisplayedQagList(thematiqueId = thematiqueId)
        return QagPreviewList(
            popularPreviewList = (popularList ?: buildPopularQags(qagList = qagList, thematiqueId = thematiqueId))
                .map { qag -> mapper.toPreview(qag, supportedQagIds, userId) },
            latestPreviewList = (latestList ?: buildLatestQags(qagList = qagList, thematiqueId = thematiqueId))
                .map { qag -> mapper.toPreview(qag, supportedQagIds, userId) },
            supportedPreviewList = (supportedQagList ?: buildSupportedQags(
                qagList = qagList,
                thematiqueId = thematiqueId,
                supportedQagIds = supportedQagIds,
                userId = userId,
            )).map { qag -> mapper.toPreview(qag, supportedQagIds, userId) },
        )
    }

    private fun buildPopularQags(
        qagList: List<QagWithSupportCount>,
        thematiqueId: String?,
    ): List<QagWithSupportCount> {
        return qagList
            .sortedByDescending { qag -> qag.supportCount }
            .take(MAX_PREVIEW_LIST_SIZE)
            .also { cacheRepository.initQagPopularList(thematiqueId, it) }
    }

    private fun buildLatestQags(
        qagList: List<QagWithSupportCount>,
        thematiqueId: String?,
    ): List<QagWithSupportCount> {
        return qagList
            .sortedByDescending { qag -> qag.qagInfo.date }
            .take(MAX_PREVIEW_LIST_SIZE)
            .also { cacheRepository.initQagLatestList(thematiqueId, it) }
    }

    private fun buildSupportedQags(
        qagList: List<QagWithSupportCount>,
        thematiqueId: String?,
        supportedQagIds: List<String>,
        userId: String,
    ): List<QagWithSupportCount> {
        val userDisplayedQagList = buildUserNotDisplayedQagList(supportedQagIds, thematiqueId, userId) + qagList
            .filter { qag -> qag.qagInfo.userId == userId }
            .sortedByDescending { qag -> qag.qagInfo.date }

        val otherQagList = supportedQagIds
            .mapNotNull { qagId -> qagList.find { it.qagInfo.id == qagId } }
            .filter { qag -> qag.qagInfo.userId != userId }

        return (userDisplayedQagList + otherQagList)
            .take(MAX_PREVIEW_LIST_SIZE)
            .also { cacheRepository.initQagSupportedList(userId, thematiqueId, it) }
    }

    private fun buildUserNotDisplayedQagList(
        supportedQagIds: List<String>,
        thematiqueId: String?,
        userId: String,
    ): List<QagWithSupportCount> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return qagInfoRepository.getUserQagInfoList(userId = userId, thematiqueId = thematiqueId)
            .mapNotNull { qagInfo ->
                thematiqueList.find { thematique -> thematique.id == qagInfo.thematiqueId }?.let { thematique ->
                    QagWithSupportCount(
                        qagInfo = qagInfo,
                        supportCount = supportedQagIds.count { supportedQagId -> supportedQagId == qagInfo.id },
                        thematique = thematique,
                    )
                }
            }
    }

}

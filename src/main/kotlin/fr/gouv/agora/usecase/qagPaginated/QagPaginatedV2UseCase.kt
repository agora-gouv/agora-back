package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class QagPaginatedV2UseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val supportQagRepository: GetSupportQagRepository,
    private val qagListsCacheRepository: QagListsCacheRepository,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getPopularQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        val userSupportedQagIds = supportQagRepository.getUserSupportedQags(userId = userId)
        val maxPageCountAndQag =
            qagListsCacheRepository.getQagPopularList(thematiqueId = thematiqueId, pageNumber = pageNumber)
        if (maxPageCountAndQag != null) {
            val qagPreviewList = maxPageCountAndQag.second.map { qagWithSupportCount ->
                mapper.toPreview(
                    qagWithSupportCount,
                    isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                    isAuthor = qagWithSupportCount.qagInfo.userId == userId
                )
            }
            return QagsAndMaxPageCount(maxPageCount = maxPageCountAndQag.first, qags = qagPreviewList)
        } else {
            val qagListAndQagPreview = getQagPaginated(
                getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getPopularQagsPaginatedV2),
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId
            )
            qagListAndQagPreview?.second?.let { qagsAndMaxPageCount ->
                qagsAndMaxPageCount.maxPageCount.let { maxPageCount ->
                    qagListsCacheRepository.initQagPopularList(
                        thematiqueId = thematiqueId,
                        pageNumber = pageNumber,
                        maxPageCount = maxPageCount,
                        qags = qagListAndQagPreview.first
                    )
                }
            }
            return qagListAndQagPreview?.second
        }
    }

    fun getLatestQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        val userSupportedQagIds = supportQagRepository.getUserSupportedQags(userId = userId)
        val maxPageCountAndQag =
            qagListsCacheRepository.getQagLatestList(thematiqueId = thematiqueId, pageNumber = pageNumber)
        if (maxPageCountAndQag != null) {
            val qagPreviewList = maxPageCountAndQag.second.map { qagWithSupportCount ->
                mapper.toPreview(
                    qagWithSupportCount,
                    isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                    isAuthor = qagWithSupportCount.qagInfo.userId == userId
                )
            }
            return QagsAndMaxPageCount(maxPageCount = maxPageCountAndQag.first, qags = qagPreviewList)
        } else {
            val qagListAndQagPreview = getQagPaginated(
                getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getLatestQagsPaginatedV2),
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId
            )
            qagListAndQagPreview?.second?.let { qagsAndMaxPageCount ->
                qagsAndMaxPageCount.maxPageCount.let { maxPageCount ->
                    qagListsCacheRepository.initQagLatestList(
                        thematiqueId = thematiqueId,
                        pageNumber = pageNumber,
                        maxPageCount = maxPageCount,
                        qags = qagListAndQagPreview.first
                    )
                }
            }
            return qagListAndQagPreview?.second
        }
    }

    fun getSupportedQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {

        val userSupportedQagIds = supportQagRepository.getUserSupportedQags(userId = userId)
        val maxPageCountAndQag =
            qagListsCacheRepository.getQagSupportedList(
                userId = userId,
                thematiqueId = thematiqueId,
                pageNumber = pageNumber
            )
        if (maxPageCountAndQag != null) {
            val qagPreviewList = maxPageCountAndQag.second.map { qagWithSupportCount ->
                mapper.toPreview(
                    qagWithSupportCount,
                    isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                    isAuthor = qagWithSupportCount.qagInfo.userId == userId
                )
            }
            return QagsAndMaxPageCount(maxPageCount = maxPageCountAndQag.first, qags = qagPreviewList)
        } else {
            val qagListAndQagPreview = getQagPaginated(
                getQagMethod = RetrieveQagMethod.WithUserId(QagInfoRepository::getSupportedQagsPaginatedV2),
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId
            )
            qagListAndQagPreview?.second?.let { qagsAndMaxPageCount ->
                qagsAndMaxPageCount.maxPageCount.let { maxPageCount ->
                    qagListsCacheRepository.initQagSupportedList(
                        userId = userId,
                        thematiqueId = thematiqueId,
                        pageNumber = pageNumber,
                        maxPageCount = maxPageCount,
                        qags = qagListAndQagPreview.first
                    )
                }
            }
            return qagListAndQagPreview?.second
        }
    }

    private fun getQagPaginated(
        getQagMethod: RetrieveQagMethod,
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): Pair<List<QagWithSupportCount>, QagsAndMaxPageCount>? {
        if (pageNumber < 1) return null
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        val userSupportedQagIds = supportQagRepository.getUserSupportedQags(userId = userId)
        val qagsCount = when (getQagMethod) {
            is RetrieveQagMethod.WithUserId -> userSupportedQagIds.size
            is RetrieveQagMethod.WithoutUserId -> qagInfoRepository.getQagsCount()
        }
        if (offset > qagsCount) return null
        val thematiques = thematiqueRepository.getThematiqueList()

        val qags = when (getQagMethod) {
            is RetrieveQagMethod.WithUserId -> {
                getQagMethod.method.invoke(
                    qagInfoRepository,
                    userId,
                    offset,
                    thematiqueId,
                )
            }

            is RetrieveQagMethod.WithoutUserId -> {
                getQagMethod.method.invoke(
                    qagInfoRepository,
                    offset,
                    thematiqueId,
                )
            }
        }
        val qagWithSupportCountList = qags.mapNotNull { qagInfoWithSupportCount ->
            thematiques.find { thematique -> thematique.id == qagInfoWithSupportCount.thematiqueId }
                ?.let { QagWithSupportCount(qagInfo = qagInfoWithSupportCount, thematique = it) }
        }
        return qagWithSupportCountList to QagsAndMaxPageCount(
            qags = qagWithSupportCountList.map { qagWithSupportCount ->
                mapper.toPreview(
                    qag = qagWithSupportCount,
                    isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                    isAuthor = qagWithSupportCount.qagInfo.userId == userId,
                )
            },
            maxPageCount = ceil(qagsCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        )
    }
}

private sealed class RetrieveQagMethod {
    data class WithoutUserId(
        val method: QagInfoRepository.(offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : RetrieveQagMethod()

    data class WithUserId(
        val method: QagInfoRepository.(userId: String, offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : RetrieveQagMethod()
}
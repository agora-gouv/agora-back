package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.infrastructure.qag.repository.QagListWithMaxPageCount
import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class QagPaginatedV2UseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val qagListsCacheRepository: QagListsCacheRepository,
    private val headerRepository: HeaderQagRepository,
    private val headerCacheRepository: HeaderQagCacheRepository,
    private val mapper: QagPreviewMapper,
    private val supportQagUseCase: SupportQagUseCase,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
        private const val TRENDING_LIST_SIZE = 20
        private const val TOP = "top"
        private const val LATEST = "latest"
        private const val SUPPORTING = "supporting"
        private const val TRENDING = "trending"
    }

    fun getPopularQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount =
            qagListsCacheRepository.getQagPopularList(thematiqueId = thematiqueId, pageNumber = pageNumber)
                ?: getQagPaginated(
                    getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getPopularQagsPaginatedV2),
                    userId = userId,
                    pageNumber = pageNumber,
                    thematiqueId = thematiqueId
                )?.also {
                    qagListsCacheRepository.initQagPopularList(
                        thematiqueId = thematiqueId,
                        pageNumber = pageNumber,
                        qagListWithMaxPageCount = it
                    )
                }
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = TOP, pageNumber = pageNumber)
    }

    fun getLatestQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount =
            qagListsCacheRepository.getQagLatestList(thematiqueId = thematiqueId, pageNumber = pageNumber)
                ?: getQagPaginated(
                    getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getLatestQagsPaginatedV2),
                    userId = userId,
                    pageNumber = pageNumber,
                    thematiqueId = thematiqueId
                )?.also {
                    qagListsCacheRepository.initQagLatestList(
                        thematiqueId = thematiqueId,
                        pageNumber = pageNumber,
                        qagListWithMaxPageCount = it
                    )
                }
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = LATEST, pageNumber = pageNumber)
    }


    fun getSupportedQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount = qagListsCacheRepository.getQagSupportedList(
            userId = userId,
            thematiqueId = thematiqueId,
            pageNumber = pageNumber
        )
            ?: getQagPaginated(
                getQagMethod = RetrieveQagMethod.WithUserId(QagInfoRepository::getSupportedQagsPaginatedV2),
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId
            )?.also {
                qagListsCacheRepository.initQagSupportedList(
                    userId = userId,
                    thematiqueId = thematiqueId,
                    pageNumber = pageNumber,
                    qagListWithMaxPageCount = it
                )
            }
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = SUPPORTING, pageNumber = pageNumber)
    }

    fun getTrendingQag(
        userId: String,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount =
            qagListsCacheRepository.getQagTrendingList()
                ?: getQagPaginated(
                    getQagMethod = RetrieveQagMethod.WithoutParams(QagInfoRepository::getTrendingQags),
                    userId = userId,
                    pageNumber = 1,
                    thematiqueId = null,
                )?.also {
                    qagListsCacheRepository.initQagTrendingList(
                        qagListWithMaxPageCount = it
                    )
                }
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = TRENDING, pageNumber = 1)
    }

    private fun getQagPaginated(
        getQagMethod: RetrieveQagMethod,
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagListWithMaxPageCount? {
        if (pageNumber < 1) return null

        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        val qagsCount = when (getQagMethod) {
            is RetrieveQagMethod.WithUserId -> supportQagUseCase.getSupportedQagCount(
                userId = userId,
                thematiqueId = thematiqueId
            )

            is RetrieveQagMethod.WithoutUserId -> qagInfoRepository.getQagsCount(thematiqueId)

            is RetrieveQagMethod.WithoutParams -> TRENDING_LIST_SIZE
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

            is RetrieveQagMethod.WithoutParams -> getQagMethod.method.invoke(qagInfoRepository)
        }
        val qagWithSupportCountList = qags.mapNotNull { qagInfoWithSupportCount ->
            thematiques.find { thematique -> thematique.id == qagInfoWithSupportCount.thematiqueId }
                ?.let { QagWithSupportCount(qagInfo = qagInfoWithSupportCount, thematique = it) }
        }
        val maxPageCount = when (getQagMethod) {
            is RetrieveQagMethod.WithoutParams -> 1
            else -> ceil(qagsCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt()
        }
        return QagListWithMaxPageCount(maxPageCount = maxPageCount, qags = qagWithSupportCountList)
    }

    private fun QagListWithMaxPageCount.mapQags(
        userId: String,
        filterType: String,
        pageNumber: Int,
    ): QagsAndMaxPageCountV2 {
        val header = when (pageNumber) {
            1 -> headerCacheRepository.getHeader(filterType) ?: headerRepository.getHeader(filterType)
                ?.also { headerCacheRepository.initHeader(filterType, it) }

            else -> null
        }
        val userSupportedQagIds = supportQagUseCase.getUserSupportedQagIds(userId)
        val qagList = this.qags.map { qagWithSupportCount ->
            mapper.toPreview(
                qag = qagWithSupportCount,
                isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                isAuthor = qagWithSupportCount.qagInfo.userId == userId
            )
        }
        return QagsAndMaxPageCountV2(
            maxPageCount = this.maxPageCount,
            headerQag = header,
            qags = qagList
        )
    }
}

data class QagsAndMaxPageCountV2(
    val qags: List<QagPreview>,
    val headerQag: HeaderQag?,
    val maxPageCount: Int,
)

private sealed class RetrieveQagMethod {
    data class WithoutUserId(
        val method: QagInfoRepository.(offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : RetrieveQagMethod()

    data class WithUserId(
        val method: QagInfoRepository.(userId: String, offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : RetrieveQagMethod()

    data class WithoutParams(
        val method: QagInfoRepository.() -> List<QagInfoWithSupportCount>,
    ) : RetrieveQagMethod()
}


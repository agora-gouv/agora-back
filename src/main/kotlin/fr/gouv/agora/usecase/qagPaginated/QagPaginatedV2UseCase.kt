package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.domain.QagWithSupportCount
import fr.gouv.agora.infrastructure.qag.repository.QagListWithMaxPageCount
import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheResult
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class QagPaginatedV2UseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
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
        val qagListWithMaxPageCount = getQagPaginated(
            getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getPopularQagsPaginatedV2),
            userId = userId,
            pageNumber = pageNumber,
            thematiqueId = thematiqueId
        )

        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = TOP, pageNumber = pageNumber)
    }

    fun getLatestQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount = getQagPaginated(
            getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getLatestQagsPaginatedV2),
            userId = userId,
            pageNumber = pageNumber,
            thematiqueId = thematiqueId
        )
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = LATEST, pageNumber = pageNumber)
    }


    fun getSupportedQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount = getQagPaginated(
            getQagMethod = RetrieveQagMethod.WithUserId(QagInfoRepository::getSupportedQagsPaginatedV2),
            userId = userId,
            pageNumber = pageNumber,
            thematiqueId = thematiqueId
        )
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = SUPPORTING, pageNumber = pageNumber)
    }

    fun getTrendingQag(
        userId: String,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount = getQagPaginated(
            getQagMethod = RetrieveQagMethod.WithoutParams(QagInfoRepository::getTrendingQags),
            userId = userId,
            pageNumber = 1,
            thematiqueId = null,
        )
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
            thematiqueRepository.getThematique(qagInfoWithSupportCount.thematiqueId)
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
            1 -> when (val cachedHeader = headerCacheRepository.getHeader(filterType)) {
                is HeaderQagCacheResult.CachedHeaderQag -> cachedHeader.headerQag
                HeaderQagCacheResult.HeaderQagCacheNotInitialized -> headerRepository.getLastHeader(filterType)
                    .also { headerQag ->
                        if (headerQag != null) headerCacheRepository.initHeader(
                            filterType,
                            headerQag
                        ) else headerCacheRepository.initHeaderNotFound(filterType)
                    }

                HeaderQagCacheResult.HeaderQagNotFound -> null
            }

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

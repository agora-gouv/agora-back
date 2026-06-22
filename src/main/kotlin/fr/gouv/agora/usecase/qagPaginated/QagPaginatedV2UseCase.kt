package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.domain.QagWithSupportCount
import fr.gouv.agora.infrastructure.qagPaginated.repository.TrendingQagCacheRepository
import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheResult
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Duration
import java.time.Instant
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class QagPaginatedV2UseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val headerRepository: HeaderQagRepository,
    private val headerCacheRepository: HeaderQagCacheRepository,
    private val trendingCacheRepository: TrendingQagCacheRepository,
    private val mapper: QagPreviewMapper,
    private val supportQagUseCase: SupportQagUseCase,
    private val clock: Clock,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
        private const val TRENDING_LIST_SIZE = 10
        private const val TOP = "top"
        private const val LATEST = "latest"
        private const val SUPPORTING = "supporting"
        private const val TRENDING = "trending"
        private const val DEFAULT_TRENDING_INTERVAL = 168
        private const val DEFAULT_TRENDING_RECENT_HOURS = 24
        private const val DEFAULT_MIN_RECENT_LIKES = 5
        private const val DEFAULT_TRENDING_SCORE_EXPONENT = 1.5
        private const val TRENDING_MAX_OLD_QAGS = 3
        private const val TRENDING_OLD_QAG_THRESHOLD_HOURS = 72L
        private const val TRENDING_SLOTS = 9
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

    fun getTrendingQag(userId: String): QagsAndMaxPageCountV2? {
        val exponent = System.getenv("TRENDING_SCORE_EXPONENT")?.toDoubleOrNull() ?: DEFAULT_TRENDING_SCORE_EXPONENT
        val now = Instant.now(clock)

        val allCandidates = when (val cached = trendingCacheRepository.getTrendingQagList()) {
            is TrendingQagCacheRepository.CacheResult.CachedTrendingQagList -> cached.qags
            TrendingQagCacheRepository.CacheResult.CacheNotInitialized -> qagInfoRepository.getTrendingQagsV3()
                .also { trendingCacheRepository.insertTrendingQagList(it) }
        }

        if (allCandidates.isEmpty()) return buildTrendingResult(emptyList(), userId)

        // Slot 1 : dernière question acceptée (déjà triée par moderated_date DESC en SQL)
        val pinned = allCandidates.first()

        // Calcul du score : (likes + 1) / (heures_depuis_acceptation + 2) ^ exponent
        fun score(qag: QagInfoWithSupportCount): Double {
            val moderatedInstant = qag.moderatedDate?.toInstant() ?: qag.date.toInstant()
            val hours = Duration.between(moderatedInstant, now).toHours().toDouble()
            return (qag.supportCount + 1).toDouble() / (hours + 2.0).pow(exponent)
        }

        // Slots 2-10 : 9 meilleurs par score hors épinglé, avec garde-fou max 3 questions > 72h
        val slots = mutableListOf<QagInfoWithSupportCount>()
        var oldQagCount = 0
        val candidates = allCandidates
            .filter { it.id != pinned.id }
            .sortedByDescending { score(it) }

        for (qag in candidates) {
            if (slots.size >= TRENDING_SLOTS) break
            val moderatedInstant = qag.moderatedDate?.toInstant() ?: qag.date.toInstant()
            val isOld = Duration.between(moderatedInstant, now).toHours() > TRENDING_OLD_QAG_THRESHOLD_HOURS
            if (isOld && oldQagCount >= TRENDING_MAX_OLD_QAGS) continue
            if (isOld) oldQagCount++
            slots.add(qag)
        }

        return buildTrendingResult(listOf(pinned) + slots, userId)
    }

    private fun buildTrendingResult(qags: List<QagInfoWithSupportCount>, userId: String): QagsAndMaxPageCountV2 {
        val qagWithSupportCountList = qags.mapNotNull { qagInfo ->
            thematiqueRepository.getThematique(qagInfo.thematiqueId)
                ?.let { QagWithSupportCount(qagInfo = qagInfo, thematique = it) }
        }
        val userSupportedQagIds = supportQagUseCase.getUserSupportedQagIds(userId)
        val header = when (val cachedHeader = headerCacheRepository.getHeader(TRENDING)) {
            is HeaderQagCacheResult.CachedHeaderQag -> cachedHeader.headerQag
            HeaderQagCacheResult.HeaderQagCacheNotInitialized -> headerRepository.getLastHeader(TRENDING)
                .also { headerQag ->
                    if (headerQag != null) headerCacheRepository.initHeader(TRENDING, headerQag)
                    else headerCacheRepository.initHeaderNotFound(TRENDING)
                }
            HeaderQagCacheResult.HeaderQagNotFound -> null
        }
        val qagList = qagWithSupportCountList.map { qagWithSupportCount ->
            mapper.toPreview(
                qag = qagWithSupportCount,
                isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                isAuthor = qagWithSupportCount.qagInfo.userId == userId,
            )
        }
        return QagsAndMaxPageCountV2(
            maxPageCount = 1,
            headerQag = header,
            qags = qagList,
        )
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

data class QagListWithMaxPageCount(
    val maxPageCount: Int,
    val qags: List<QagWithSupportCount>,
)

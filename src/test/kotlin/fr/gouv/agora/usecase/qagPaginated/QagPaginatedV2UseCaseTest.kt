package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.QagWithSupportCount
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.qagPaginated.repository.TrendingQagCacheRepository
import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheResult
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Date

@ExtendWith(MockitoExtension::class)
internal class QagPaginatedV2UseCaseTest {

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Mock
    private lateinit var thematiqueRepository: ThematiqueRepository

    @Mock
    private lateinit var headerRepository: HeaderQagRepository

    @Mock
    private lateinit var headerCacheRepository: HeaderQagCacheRepository

    @Mock
    private lateinit var trendingCacheRepository: TrendingQagCacheRepository

    @Mock
    private lateinit var mapper: QagPreviewMapper

    @Mock
    private lateinit var supportQagUseCase: SupportQagUseCase

    // Fixed clock: Monday 2024-01-08 at 12:00
    private val fixedNow = LocalDateTime.of(2024, 1, 8, 12, 0, 0)
    private val clock = TestUtils.getFixedClock(fixedNow)

    private lateinit var useCase: QagPaginatedV2UseCase

    @BeforeEach
    fun setUp() {
        useCase = QagPaginatedV2UseCase(
            qagInfoRepository = qagInfoRepository,
            thematiqueRepository = thematiqueRepository,
            headerRepository = headerRepository,
            headerCacheRepository = headerCacheRepository,
            trendingCacheRepository = trendingCacheRepository,
            mapper = mapper,
            supportQagUseCase = supportQagUseCase,
            clock = clock,
        )
    }

    private val thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto")

    /**
     * Builds a QagInfoWithSupportCount with a moderatedDate offset from fixedNow.
     * @param hoursAgo how many hours before fixedNow the qag was moderated
     */
    private fun buildQag(
        id: String = "qagId",
        thematiqueId: String = "thematiqueId",
        userId: String = "authorId",
        supportCount: Int = 10,
        hoursAgo: Long = 1L,
    ): QagInfoWithSupportCount {
        val moderatedDate = Date.from(
            fixedNow.minusHours(hoursAgo).atZone(java.time.ZoneId.systemDefault()).toInstant()
        )
        return QagInfoWithSupportCount(
            id = id,
            thematiqueId = thematiqueId,
            title = "title",
            description = "description",
            date = moderatedDate,
            status = QagStatus.MODERATED_ACCEPTED,
            username = "username",
            userId = userId,
            supportCount = supportCount,
            moderatedDate = moderatedDate,
        )
    }

    private fun stubCommonMocks(qags: List<QagInfoWithSupportCount> = emptyList()) {
        given(trendingCacheRepository.getTrendingQagList())
            .willReturn(TrendingQagCacheRepository.CacheResult.CacheNotInitialized)
        given(qagInfoRepository.getTrendingQagsV3()).willReturn(qags)
        given(headerCacheRepository.getHeader("trending"))
            .willReturn(HeaderQagCacheResult.HeaderQagNotFound)
        given(supportQagUseCase.getUserSupportedQagIds(userId = "userId"))
            .willReturn(emptyList())
        qags.forEach { qag ->
            given(thematiqueRepository.getThematique(qag.thematiqueId)).willReturn(thematique)
        }
    }

    // Kotlin/Mockito helper: any() returns null which breaks non-nullable params
    private fun anyQagWithSupportCount(): QagWithSupportCount =
        ArgumentMatchers.any(QagWithSupportCount::class.java) ?: QagWithSupportCount(
            qagInfo = buildQag(),
            thematique = thematique,
        )

    private fun stubMapperForAnyQag(): QagPreview {
        val preview = mock(QagPreview::class.java)
        doReturn(preview).`when`(mapper).toPreview(
            anyQagWithSupportCount(),
            anyBoolean(),
            anyBoolean(),
        )
        return preview
    }

    @Nested
    inner class GetTrendingQagTests {

        @Nested
        inner class CacheTests {

            @Test
            fun `getTrendingQag - when cache is not initialized - should call repository and populate cache`() {
                // Given
                given(trendingCacheRepository.getTrendingQagList())
                    .willReturn(TrendingQagCacheRepository.CacheResult.CacheNotInitialized)
                given(qagInfoRepository.getTrendingQagsV3()).willReturn(emptyList())
                given(headerCacheRepository.getHeader("trending"))
                    .willReturn(HeaderQagCacheResult.HeaderQagNotFound)
                given(supportQagUseCase.getUserSupportedQagIds(userId = "userId"))
                    .willReturn(emptyList())

                // When
                useCase.getTrendingQag(userId = "userId")

                // Then
                then(qagInfoRepository).should().getTrendingQagsV3()
                then(trendingCacheRepository).should().insertTrendingQagList(emptyList())
            }

            @Test
            fun `getTrendingQag - when cache is populated - should not call repository`() {
                // Given
                val qag = buildQag(hoursAgo = 1L)
                given(trendingCacheRepository.getTrendingQagList())
                    .willReturn(TrendingQagCacheRepository.CacheResult.CachedTrendingQagList(listOf(qag)))
                given(headerCacheRepository.getHeader("trending"))
                    .willReturn(HeaderQagCacheResult.HeaderQagNotFound)
                given(supportQagUseCase.getUserSupportedQagIds(userId = "userId"))
                    .willReturn(emptyList())
                given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
                stubMapperForAnyQag()

                // When
                useCase.getTrendingQag(userId = "userId")

                // Then
                then(qagInfoRepository).shouldHaveNoInteractions()
                then(trendingCacheRepository).should(never()).insertTrendingQagList(anyList())
            }
        }

        @Nested
        inner class EmptyListTests {

            @Test
            fun `getTrendingQag - when repository returns empty list - should return result with empty qag list`() {
                // Given
                stubCommonMocks(emptyList())

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then
                assertThat(result).isNotNull
                assertThat(result!!.qags).isEmpty()
                assertThat(result.maxPageCount).isEqualTo(1)
            }
        }

        @Nested
        inner class PinnedSlotTests {

            @Test
            fun `getTrendingQag - when repository returns qags - should pin the most recent as first slot`() {
                // Given
                val newestQag = buildQag(id = "newest", hoursAgo = 1L, supportCount = 0)
                val olderQag = buildQag(id = "older", hoursAgo = 10L, supportCount = 100)
                stubCommonMocks(listOf(newestQag, olderQag))

                val pinnedPreview = mock(QagPreview::class.java)
                val olderPreview = mock(QagPreview::class.java)
                given(
                    mapper.toPreview(
                        qag = QagWithSupportCount(newestQag, thematique),
                        isSupportedByUser = false,
                        isAuthor = false,
                    )
                ).willReturn(pinnedPreview)
                given(
                    mapper.toPreview(
                        qag = QagWithSupportCount(olderQag, thematique),
                        isSupportedByUser = false,
                        isAuthor = false,
                    )
                ).willReturn(olderPreview)

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then
                assertThat(result).isNotNull
                assertThat(result!!.qags.first()).isEqualTo(pinnedPreview)
            }

            @Test
            fun `getTrendingQag - when only one qag - should return only the pinned slot`() {
                // Given
                val singleQag = buildQag(id = "single", hoursAgo = 2L)
                stubCommonMocks(listOf(singleQag))
                stubMapperForAnyQag()

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then
                assertThat(result!!.qags).hasSize(1)
            }
        }

        @Nested
        inner class ScoreOrderingTests {

            @Test
            fun `getTrendingQag - slots 2-10 should be ordered by descending score`() {
                // Given
                // pinned = most recent (hoursAgo=1)
                // highScore: 51 likes, 2h ago → score = 52 / 4^1.5 = 52/8 = 6.5
                // lowScore: 1 like, 50h ago → score = 2 / 52^1.5 ≈ 0.0053
                val pinned = buildQag(id = "pinned", hoursAgo = 1L, supportCount = 0)
                val highScore = buildQag(id = "highScore", hoursAgo = 2L, supportCount = 51)
                val lowScore = buildQag(id = "lowScore", hoursAgo = 50L, supportCount = 1)
                stubCommonMocks(listOf(pinned, highScore, lowScore))

                val pinnedPreview = mock(QagPreview::class.java)
                val highPreview = mock(QagPreview::class.java)
                val lowPreview = mock(QagPreview::class.java)
                given(
                    mapper.toPreview(
                        qag = QagWithSupportCount(pinned, thematique),
                        isSupportedByUser = false,
                        isAuthor = false,
                    )
                ).willReturn(pinnedPreview)
                given(
                    mapper.toPreview(
                        qag = QagWithSupportCount(highScore, thematique),
                        isSupportedByUser = false,
                        isAuthor = false,
                    )
                ).willReturn(highPreview)
                given(
                    mapper.toPreview(
                        qag = QagWithSupportCount(lowScore, thematique),
                        isSupportedByUser = false,
                        isAuthor = false,
                    )
                ).willReturn(lowPreview)

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then
                assertThat(result!!.qags).containsExactly(pinnedPreview, highPreview, lowPreview)
            }
        }

        @Nested
        inner class AntiMonopolyGuardTests {

            @Test
            fun `getTrendingQag - when more than 3 qags are older than 72h - should cap old qags at 3 in slots 2-10`() {
                // Given
                val pinned = buildQag(id = "pinned", hoursAgo = 1L, supportCount = 0)
                // 5 old qags (> 72h), with descending support count to ensure score ordering
                val old1 = buildQag(id = "old1", hoursAgo = 100L, supportCount = 50)
                val old2 = buildQag(id = "old2", hoursAgo = 101L, supportCount = 40)
                val old3 = buildQag(id = "old3", hoursAgo = 102L, supportCount = 30)
                val old4 = buildQag(id = "old4", hoursAgo = 103L, supportCount = 20)
                val old5 = buildQag(id = "old5", hoursAgo = 104L, supportCount = 10)
                // 1 fresh qag (< 72h)
                val fresh = buildQag(id = "fresh", hoursAgo = 5L, supportCount = 1)

                stubCommonMocks(listOf(pinned, old1, old2, old3, old4, old5, fresh))
                stubMapperForAnyQag()

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then: pinned(1) + old1(2) + old2(3) + old3(4) + fresh(5) → old4 and old5 skipped
                assertThat(result!!.qags).hasSize(5)
            }

            @Test
            fun `getTrendingQag - when fewer than 3 qags are older than 72h - should include all of them`() {
                // Given
                val pinned = buildQag(id = "pinned", hoursAgo = 1L, supportCount = 0)
                val old1 = buildQag(id = "old1", hoursAgo = 100L, supportCount = 50)
                val old2 = buildQag(id = "old2", hoursAgo = 101L, supportCount = 40)
                val fresh = buildQag(id = "fresh", hoursAgo = 5L, supportCount = 1)
                stubCommonMocks(listOf(pinned, old1, old2, fresh))
                stubMapperForAnyQag()

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then: pinned + old1 + old2 + fresh = 4 total
                assertThat(result!!.qags).hasSize(4)
            }
        }

        @Nested
        inner class UserContextTests {

            @Test
            fun `getTrendingQag - when user supports a qag - should set isSupportedByUser to true`() {
                // Given
                val qag = buildQag(id = "qagId", userId = "authorId", hoursAgo = 1L)
                given(trendingCacheRepository.getTrendingQagList())
                    .willReturn(TrendingQagCacheRepository.CacheResult.CacheNotInitialized)
                given(qagInfoRepository.getTrendingQagsV3()).willReturn(listOf(qag))
                given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
                given(headerCacheRepository.getHeader("trending"))
                    .willReturn(HeaderQagCacheResult.HeaderQagNotFound)
                given(supportQagUseCase.getUserSupportedQagIds(userId = "userId"))
                    .willReturn(listOf("qagId"))

                val qagPreview = mock(QagPreview::class.java)
                given(
                    mapper.toPreview(
                        qag = QagWithSupportCount(qagInfo = qag, thematique = thematique),
                        isSupportedByUser = true,
                        isAuthor = false,
                    )
                ).willReturn(qagPreview)

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then
                assertThat(result!!.qags).isEqualTo(listOf(qagPreview))
            }

            @Test
            fun `getTrendingQag - when user is author of a qag - should set isAuthor to true`() {
                // Given
                val qag = buildQag(id = "qagId", userId = "userId", hoursAgo = 1L)
                given(trendingCacheRepository.getTrendingQagList())
                    .willReturn(TrendingQagCacheRepository.CacheResult.CacheNotInitialized)
                given(qagInfoRepository.getTrendingQagsV3()).willReturn(listOf(qag))
                given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
                given(headerCacheRepository.getHeader("trending"))
                    .willReturn(HeaderQagCacheResult.HeaderQagNotFound)
                given(supportQagUseCase.getUserSupportedQagIds(userId = "userId"))
                    .willReturn(emptyList())

                val qagPreview = mock(QagPreview::class.java)
                given(
                    mapper.toPreview(
                        qag = QagWithSupportCount(qagInfo = qag, thematique = thematique),
                        isSupportedByUser = false,
                        isAuthor = true,
                    )
                ).willReturn(qagPreview)

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then
                assertThat(result!!.qags).isEqualTo(listOf(qagPreview))
            }

            @Test
            fun `getTrendingQag - when qag has unknown thematique - should exclude it from result`() {
                // Given
                val qag = buildQag(id = "qagId", thematiqueId = "unknownThematiqueId", hoursAgo = 1L)
                given(trendingCacheRepository.getTrendingQagList())
                    .willReturn(TrendingQagCacheRepository.CacheResult.CacheNotInitialized)
                given(qagInfoRepository.getTrendingQagsV3()).willReturn(listOf(qag))
                given(thematiqueRepository.getThematique("unknownThematiqueId")).willReturn(null)
                given(headerCacheRepository.getHeader("trending"))
                    .willReturn(HeaderQagCacheResult.HeaderQagNotFound)
                given(supportQagUseCase.getUserSupportedQagIds(userId = "userId"))
                    .willReturn(emptyList())

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then
                assertThat(result!!.qags).isEmpty()
                then(mapper).shouldHaveNoInteractions()
            }
        }

        @Nested
        inner class MaxSlotsTests {

            @Test
            fun `getTrendingQag - when more than 10 qags available - should return at most 10`() {
                // Given
                val qags = (1..15).map { i ->
                    buildQag(id = "qag$i", hoursAgo = i.toLong(), supportCount = 100 - i)
                }
                stubCommonMocks(qags)
                stubMapperForAnyQag()

                // When
                val result = useCase.getTrendingQag(userId = "userId")

                // Then: 1 pinned + 9 slots = 10 max
                assertThat(result!!.qags).hasSize(10)
            }
        }
    }
}

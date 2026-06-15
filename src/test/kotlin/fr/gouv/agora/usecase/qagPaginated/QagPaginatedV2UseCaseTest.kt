package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.QagWithSupportCount
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagCacheResult
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Date
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@ExtendWith(MockitoExtension::class)
internal class QagPaginatedV2UseCaseTest {

    @InjectMocks
    private lateinit var useCase: QagPaginatedV2UseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Mock
    private lateinit var thematiqueRepository: ThematiqueRepository

    @Mock
    private lateinit var headerRepository: HeaderQagRepository

    @Mock
    private lateinit var headerCacheRepository: HeaderQagCacheRepository

    @Mock
    private lateinit var mapper: QagPreviewMapper

    @Mock
    private lateinit var supportQagUseCase: SupportQagUseCase

    // Duration is a Kotlin inline class; org.mockito.ArgumentMatchers.any<Duration>() returns Duration?
    private fun anyDuration(): Duration = org.mockito.ArgumentMatchers.any<Duration>() ?: Duration.ZERO

    private fun buildQagInfoWithSupportCount(
        id: String = "qagId",
        thematiqueId: String = "thematiqueId",
        userId: String = "authorId",
    ) = QagInfoWithSupportCount(
        id = id,
        thematiqueId = thematiqueId,
        title = "title",
        description = "description",
        date = Date(),
        status = QagStatus.MODERATED_ACCEPTED,
        username = "username",
        userId = userId,
        supportCount = 10,
    )

    @Nested
    inner class GetTrendingQagTests {

        @Test
        fun `getTrendingQag - when repository returns empty list - should return result with empty qag list`() {
            // Given
            doReturn(emptyList<QagInfoWithSupportCount>())
                .`when`(qagInfoRepository).getTrendingQagsWithRecentLikes(anyDuration(), anyInt())
            given(headerCacheRepository.getHeader("trending")).willReturn(HeaderQagCacheResult.HeaderQagNotFound)
            given(supportQagUseCase.getUserSupportedQagIds(userId = "userId")).willReturn(emptyList())

            // When
            val result = useCase.getTrendingQag(userId = "userId")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.qags).isEmpty()
            assertThat(result.maxPageCount).isEqualTo(1)
        }

        @Test
        fun `getTrendingQag - when repository returns qags with known thematique - should return mapped qag list`() {
            // Given
            val thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto")
            val qagInfoWithSupportCount = buildQagInfoWithSupportCount(thematiqueId = "thematiqueId", userId = "authorId")

            doReturn(listOf(qagInfoWithSupportCount))
                .`when`(qagInfoRepository).getTrendingQagsWithRecentLikes(anyDuration(), anyInt())
            given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
            given(headerCacheRepository.getHeader("trending")).willReturn(HeaderQagCacheResult.HeaderQagNotFound)
            given(supportQagUseCase.getUserSupportedQagIds(userId = "userId")).willReturn(emptyList())

            val qagPreview = mock(QagPreview::class.java)
            given(
                mapper.toPreview(
                    qag = QagWithSupportCount(qagInfo = qagInfoWithSupportCount, thematique = thematique),
                    isSupportedByUser = false,
                    isAuthor = false,
                )
            ).willReturn(qagPreview)

            // When
            val result = useCase.getTrendingQag(userId = "userId")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.qags).isEqualTo(listOf(qagPreview))
            assertThat(result.maxPageCount).isEqualTo(1)
        }

        @Test
        fun `getTrendingQag - when repository returns qag with unknown thematique - should return empty qag list`() {
            // Given
            val qagInfoWithSupportCount = buildQagInfoWithSupportCount(thematiqueId = "unknownThematiqueId")

            doReturn(listOf(qagInfoWithSupportCount))
                .`when`(qagInfoRepository).getTrendingQagsWithRecentLikes(anyDuration(), anyInt())
            given(thematiqueRepository.getThematique("unknownThematiqueId")).willReturn(null)
            given(headerCacheRepository.getHeader("trending")).willReturn(HeaderQagCacheResult.HeaderQagNotFound)
            given(supportQagUseCase.getUserSupportedQagIds(userId = "userId")).willReturn(emptyList())

            // When
            val result = useCase.getTrendingQag(userId = "userId")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.qags).isEmpty()
            assertThat(result.maxPageCount).isEqualTo(1)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getTrendingQag - when user supports a qag - should set isSupportedByUser to true`() {
            // Given
            val thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto")
            val qagInfoWithSupportCount = buildQagInfoWithSupportCount(id = "qagId", thematiqueId = "thematiqueId", userId = "authorId")

            doReturn(listOf(qagInfoWithSupportCount))
                .`when`(qagInfoRepository).getTrendingQagsWithRecentLikes(anyDuration(), anyInt())
            given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
            given(headerCacheRepository.getHeader("trending")).willReturn(HeaderQagCacheResult.HeaderQagNotFound)
            given(supportQagUseCase.getUserSupportedQagIds(userId = "userId")).willReturn(listOf("qagId"))

            val qagPreview = mock(QagPreview::class.java)
            given(
                mapper.toPreview(
                    qag = QagWithSupportCount(qagInfo = qagInfoWithSupportCount, thematique = thematique),
                    isSupportedByUser = true,
                    isAuthor = false,
                )
            ).willReturn(qagPreview)

            // When
            val result = useCase.getTrendingQag(userId = "userId")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.qags).isEqualTo(listOf(qagPreview))
        }

        @Test
        fun `getTrendingQag - when user is author of a qag - should set isAuthor to true`() {
            // Given
            val thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto")
            val qagInfoWithSupportCount = buildQagInfoWithSupportCount(thematiqueId = "thematiqueId", userId = "userId")

            doReturn(listOf(qagInfoWithSupportCount))
                .`when`(qagInfoRepository).getTrendingQagsWithRecentLikes(anyDuration(), anyInt())
            given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
            given(headerCacheRepository.getHeader("trending")).willReturn(HeaderQagCacheResult.HeaderQagNotFound)
            given(supportQagUseCase.getUserSupportedQagIds(userId = "userId")).willReturn(emptyList())

            val qagPreview = mock(QagPreview::class.java)
            given(
                mapper.toPreview(
                    qag = QagWithSupportCount(qagInfo = qagInfoWithSupportCount, thematique = thematique),
                    isSupportedByUser = false,
                    isAuthor = true,
                )
            ).willReturn(qagPreview)

            // When
            val result = useCase.getTrendingQag(userId = "userId")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.qags).isEqualTo(listOf(qagPreview))
        }
    }
}

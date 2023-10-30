package fr.social.gouv.agora.usecase.qagPaginated

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class QagPaginatedUseCaseTest {

    @Autowired
    private lateinit var useCase: QagPaginatedUseCase

    @MockBean
    private lateinit var filterGenerator: QagPaginatedFilterGenerator

    @MockBean
    private lateinit var mapper: QagPreviewMapper

    private val userId = "userId"
    private val thematiqueId = "thematiqueId"
    private val keywords = "keywords"

//    @Nested
//    inner class PopularQagPaginatedTestCases {
//
//        @Test
//        fun `getPopularQagPaginated - when pageNumber is lower or equals 0 - should return null`() {
//            // When
//            val result = useCase.getPopularQagPaginated(
//                userId = userId,
//                pageNumber = 0,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(null)
//            then(getQagListUseCase).shouldHaveNoInteractions()
//            then(mapper).shouldHaveNoInteractions()
//        }
//
//        @Test
//        fun `getPopularQagPaginated - when pageNumber is higher than maxPageNumber - should return null`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 3, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//            val qagList = (0 until 21).map { mock(QagInfoWithSupportAndThematique::class.java) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters)).willReturn(qagList)
//
//            // When
//            val result = useCase.getPopularQagPaginated(
//                userId = userId,
//                pageNumber = 3,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(null)
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 3, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            then(mapper).shouldHaveNoInteractions()
//        }
//
//        @Test
//        fun `getPopularQagPaginated - when have less than 20 results and pageNumber is 1 - should return ordered qags with highest supportCount and maxPageNumber 1`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//
//            val (qag6Supports, qagPreview6Supports) = mockQag(supportCount = 6)
//            val (qag42Supports, qagPreview42Supports) = mockQag(supportCount = 42)
//            val (qag11Supports, qagPreview11Supports) = mockQag(supportCount = 11)
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(listOf(qag6Supports, qag42Supports, qag11Supports))
//
//            // When
//            val result = useCase.getPopularQagPaginated(
//                userId = userId,
//                pageNumber = 1,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = listOf(qagPreview42Supports, qagPreview11Supports, qagPreview6Supports),
//                    maxPageCount = 1,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            then(mapper).should().toPreview(qag = qag42Supports, userId = userId)
//            then(mapper).should().toPreview(qag = qag11Supports, userId = userId)
//            then(mapper).should().toPreview(qag = qag6Supports, userId = userId)
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        @Test
//        fun `getPopularQagPaginated - when have more than 20 results and pageNumber is 1 - should return ordered qags with highest supportCount limited to 20`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//
//            val firstPageQags = (20 downTo 1).map { index -> mockQag(supportCount = index) }
//            val otherPageQags = (0 until 99).map { mockQag(supportCount = 0) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(firstPageQags.map { it.first } + otherPageQags.map { it.first })
//
//            // When
//            val result = useCase.getPopularQagPaginated(
//                userId = userId,
//                pageNumber = 1,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result?.qags?.size).isEqualTo(20)
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = firstPageQags.map { it.second },
//                    maxPageCount = 6,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            firstPageQags.map { (qag, _) ->
//                then(mapper).should().toPreview(qag = qag, userId = userId)
//            }
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        @Test
//        fun `getPopularQagPaginated - when has not enough to fill page 2 entirely and pageNumber is 2 - should return ordered qags with highest supportCount starting from 20th index and limited to 20 items or less`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 2, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//
//            val firstPageQags = (35 downTo 16).map { index -> mockQag(supportCount = index) }
//            val secondPageQags = (15 downTo 0).map { index -> mockQag(supportCount = index) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(firstPageQags.map { it.first } + secondPageQags.map { it.first })
//
//            // When
//            val result = useCase.getPopularQagPaginated(
//                userId = userId,
//                pageNumber = 2,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//
//            assertThat(result?.qags?.size).isEqualTo(16)
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = secondPageQags.map { it.second },
//                    maxPageCount = 2,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 2, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            secondPageQags.map { (qag, _) ->
//                then(mapper).should().toPreview(qag = qag, userId = userId)
//            }
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        private fun mockQag(supportCount: Int): Pair<QagInfoWithSupportAndThematique, QagPreview> {
//            val supportQagInfoList = (0 until supportCount).map { mock(SupportQagInfo::class.java) }
//            val qag = mock(QagInfoWithSupportAndThematique::class.java).also {
//                given(it.supportQagInfoList).willReturn(supportQagInfoList)
//            }
//
//            val qagPreview = mock(QagPreview::class.java)
//            given(mapper.toPreview(qag = qag, userId = userId)).willReturn(qagPreview)
//
//            return qag to qagPreview
//        }
//
//    }
//
//    @Nested
//    inner class LatestQagPaginatedTestCases {
//
//        @Test
//        fun `getLatestQagPaginated - when pageNumber is lower or equals 0 - should return null`() {
//            // When
//            val result = useCase.getLatestQagPaginated(
//                userId = userId,
//                pageNumber = 0,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(null)
//            then(getQagListUseCase).shouldHaveNoInteractions()
//            then(mapper).shouldHaveNoInteractions()
//        }
//
//        @Test
//        fun `getLatestQagPaginated - when pageNumber is higher than maxPageNumber - should return null`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 3, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//            val qagList = (0 until 21).map { mock(QagInfoWithSupportAndThematique::class.java) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters)).willReturn(qagList)
//
//            // When
//            val result = useCase.getLatestQagPaginated(
//                userId = userId,
//                pageNumber = 3,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(null)
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 3, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            then(mapper).shouldHaveNoInteractions()
//        }
//
//        @Test
//        fun `getLatestQagPaginated - when have less than 20 results and pageNumber is 1 - should return ordered qags with most recent postDate with maxPageNumber 1`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//
//            val (qagDate6, qagPreviewDate6) = mockQag(postDate = Date(6))
//            val (qagDate42, qagPreviewDate42) = mockQag(postDate = Date(42))
//            val (qagDate11, qagPreviewDate11) = mockQag(postDate = Date(11))
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(listOf(qagDate6, qagDate42, qagDate11))
//
//            // When
//            val result = useCase.getLatestQagPaginated(
//                userId = userId,
//                pageNumber = 1,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = listOf(qagPreviewDate42, qagPreviewDate11, qagPreviewDate6),
//                    maxPageCount = 1,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            then(mapper).should().toPreview(qag = qagDate42, userId = userId)
//            then(mapper).should().toPreview(qag = qagDate11, userId = userId)
//            then(mapper).should().toPreview(qag = qagDate6, userId = userId)
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        @Test
//        fun `getLatestQagPaginated - when have more than 20 results and pageNumber is 1 - should return ordered qags with most recent postDate limited to 20`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//
//            val firstPageQags = (20 downTo 1).map { index -> mockQag(postDate = Date(index.toLong())) }
//            val otherPageQags = (0 until 99).map { mockQag(postDate = Date(0)) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(firstPageQags.map { it.first } + otherPageQags.map { it.first })
//
//            // When
//            val result = useCase.getLatestQagPaginated(
//                userId = userId,
//                pageNumber = 1,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result?.qags?.size).isEqualTo(20)
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = firstPageQags.map { it.second },
//                    maxPageCount = 6,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            firstPageQags.map { (qag, _) ->
//                then(mapper).should().toPreview(qag = qag, userId = userId)
//            }
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        @Test
//        fun `getLatestQagPaginated - when has not enough to fill page 2 entirely and pageNumber is 2 - should return ordered qags with most recent postDate starting from 20th index and limited to 20 items or less`() {
//            // Given
//            given(filterGenerator.getPaginatedQagFilters(userId = userId, pageNumber = 2, thematiqueId = thematiqueId, keywords = keywords))
//                .willReturn(qagFilters)
//
//            val firstPageQags = (35 downTo 16).map { index -> mockQag(postDate = Date(index.toLong())) }
//            val secondPageQags = (15 downTo 0).map { index -> mockQag(postDate = Date(index.toLong())) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(firstPageQags.map { it.first } + secondPageQags.map { it.first })
//
//            // When
//            val result = useCase.getLatestQagPaginated(
//                userId = userId,
//                pageNumber = 2,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//
//            assertThat(result?.qags?.size).isEqualTo(16)
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = secondPageQags.map { it.second },
//                    maxPageCount = 2,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getPaginatedQagFilters(userId = userId, pageNumber = 2, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            secondPageQags.map { (qag, _) ->
//                then(mapper).should().toPreview(qag = qag, userId = userId)
//            }
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        private fun mockQag(postDate: Date): Pair<QagInfoWithSupportAndThematique, QagPreview> {
//            val qagInfo = mock(QagInfo::class.java).also {
//                given(it.date).willReturn(postDate)
//            }
//            val qag = mock(QagInfoWithSupportAndThematique::class.java).also {
//                given(it.qagInfo).willReturn(qagInfo)
//            }
//
//            val qagPreview = mock(QagPreview::class.java)
//            given(mapper.toPreview(qag = qag, userId = userId)).willReturn(qagPreview)
//
//            return qag to qagPreview
//        }
//
//    }
//
//    @Nested
//    inner class SupportedQagPaginatedTestCases {
//
//        @Test
//        fun `getSupportedQagPaginated - when pageNumber is lower or equals 0 - should return null`() {
//            // When
//            val result = useCase.getSupportedQagPaginated(
//                userId = userId,
//                pageNumber = 0,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(null)
//            then(getQagListUseCase).shouldHaveNoInteractions()
//            then(mapper).shouldHaveNoInteractions()
//        }
//
//        @Test
//        fun `getSupportedQagPaginated - when pageNumber is higher than maxPageNumber - should return null`() {
//            // Given
//            given(
//                filterGenerator.getSupportedPaginatedQagFilters(
//                    userId = userId,
//                    pageNumber = 3,
//                    thematiqueId = thematiqueId,
//                    keywords = keywords,
//                )
//            ).willReturn(qagFilters)
//            val qagList = (0 until 21).map { mock(QagInfoWithSupportAndThematique::class.java) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters)).willReturn(qagList)
//
//            // When
//            val result = useCase.getSupportedQagPaginated(
//                userId = userId,
//                pageNumber = 3,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(null)
//            then(filterGenerator).should(only())
//                .getSupportedPaginatedQagFilters(userId = userId, pageNumber = 3, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            then(mapper).shouldHaveNoInteractions()
//        }
//
//        @Test
//        fun `getSupportedQagPaginated - when have less than 20 results and pageNumber is 1 - should return ordered qags with most recent supportDate and maxPageNumber 1`() {
//            // Given
//            given(
//                filterGenerator.getSupportedPaginatedQagFilters(
//                    userId = userId,
//                    pageNumber = 1,
//                    thematiqueId = thematiqueId,
//                    keywords = keywords,
//                )
//            ).willReturn(qagFilters)
//
//            val (userQag1, userQagPreview1) = mockQag(postDate = Date(1))
//            val (userQag2, userQagPreview2) = mockQag(postDate = Date(9))
//            val (qagDate6, qagPreviewDate6) = mockQag(supportDate = Date(6), authorUserId = "userId1")
//            val (qagDate42, qagPreviewDate42) = mockQag(supportDate = Date(42), authorUserId = "userId2")
//            val (qagDate11, qagPreviewDate11) = mockQag(supportDate = Date(11), authorUserId = "userId3")
//
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(listOf(userQag1, userQag2, qagDate6, qagDate42, qagDate11))
//
//            // When
//            val result = useCase.getSupportedQagPaginated(
//                userId = userId,
//                pageNumber = 1,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = listOf(
//                        userQagPreview2,
//                        userQagPreview1,
//                        qagPreviewDate42,
//                        qagPreviewDate11,
//                        qagPreviewDate6
//                    ),
//                    maxPageCount = 1,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getSupportedPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            then(mapper).should().toPreview(qag = userQag2, userId = userId)
//            then(mapper).should().toPreview(qag = userQag1, userId = userId)
//            then(mapper).should().toPreview(qag = qagDate42, userId = userId)
//            then(mapper).should().toPreview(qag = qagDate11, userId = userId)
//            then(mapper).should().toPreview(qag = qagDate6, userId = userId)
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        @Test
//        fun `getSupportedQagPaginated - when have more than 20 results and pageNumber is 1 - should return ordered qags with most recent supportDate limited to 20`() {
//            // Given
//            given(
//                filterGenerator.getSupportedPaginatedQagFilters(
//                    userId = userId,
//                    pageNumber = 1,
//                    thematiqueId = thematiqueId,
//                    keywords = keywords,
//                )
//            ).willReturn(qagFilters)
//
//            val firstPageQags = (20 downTo 1).map { index -> mockQag(supportDate = Date(index.toLong())) }
//            val otherPageQags = (0 until 99).map { mockQag(supportDate = Date(0)) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(firstPageQags.map { it.first } + otherPageQags.map { it.first })
//
//            // When
//            val result = useCase.getSupportedQagPaginated(
//                userId = userId,
//                pageNumber = 1,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result?.qags?.size).isEqualTo(20)
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = firstPageQags.map { it.second },
//                    maxPageCount = 6,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getSupportedPaginatedQagFilters(userId = userId, pageNumber = 1, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            firstPageQags.map { (qag, _) ->
//                then(mapper).should().toPreview(qag = qag, userId = userId)
//            }
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        @Test
//        fun `getSupportedQagPaginated - when has not enough to fill page 2 entirely and pageNumber is 2 - should return ordered qags with most recent supportDate starting from 20th index and limited to 20 items or less`() {
//            // Given
//            given(
//                filterGenerator.getSupportedPaginatedQagFilters(
//                    userId = userId,
//                    pageNumber = 2,
//                    thematiqueId = thematiqueId,
//                    keywords = keywords,
//                )
//            ).willReturn(qagFilters)
//
//            val firstPageQags = (35 downTo 16).map { index -> mockQag(supportDate = Date(index.toLong())) }
//            val secondPageQags = (15 downTo 0).map { index -> mockQag(supportDate = Date(index.toLong())) }
//            given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters = qagFilters))
//                .willReturn(firstPageQags.map { it.first } + secondPageQags.map { it.first })
//
//            // When
//            val result = useCase.getSupportedQagPaginated(
//                userId = userId,
//                pageNumber = 2,
//                thematiqueId = thematiqueId,
//                keywords = keywords,
//            )
//
//            // Then
//            assertThat(result?.qags?.size).isEqualTo(16)
//            assertThat(result).isEqualTo(
//                QagsAndMaxPageCount(
//                    qags = secondPageQags.map { it.second },
//                    maxPageCount = 2,
//                )
//            )
//            then(filterGenerator).should(only())
//                .getSupportedPaginatedQagFilters(userId = userId, pageNumber = 2, thematiqueId = thematiqueId, keywords = keywords)
//            then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters = qagFilters)
//            secondPageQags.map { (qag, _) ->
//                then(mapper).should().toPreview(qag = qag, userId = userId)
//            }
//            then(mapper).shouldHaveNoMoreInteractions()
//        }
//
//        private fun mockQag(
//            supportDate: Date = Date(0),
//            postDate: Date = Date(0),
//            authorUserId: String = "userId",
//            supportUserId: String = "userId",
//        ): Pair<QagInfoWithSupportAndThematique, QagPreview> {
//            val supportQagInfo = mock(SupportQagInfo::class.java).also {
//                given(it.supportDate).willReturn(supportDate)
//                given(it.userId).willReturn(supportUserId)
//            }
//
//            val qagInfo = mock(QagInfo::class.java).also {
//                given(it.date).willReturn(postDate)
//                given(it.userId).willReturn(authorUserId)
//            }
//            val qag = mock(QagInfoWithSupportAndThematique::class.java).also {
//                given(it.supportQagInfoList).willReturn(listOf(supportQagInfo))
//                given(it.qagInfo).willReturn(qagInfo)
//            }
//
//            val qagPreview = mock(QagPreview::class.java)
//            given(mapper.toPreview(qag = qag, userId = supportUserId)).willReturn(qagPreview)
//
//            return qag to qagPreview
//        }
//    }
}
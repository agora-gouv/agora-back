package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.domain.FeedbackResults
import fr.social.gouv.agora.usecase.feedbackQag.repository.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class FeedbackQagUseCaseTest {

    companion object {
        @JvmStatic
        fun getFeedbackResultsTestCases() = arrayOf(
            input(
                helpfulFeedbackCount = 0,
                notHelpfulFeedbackCount = 0,
                expectedPositiveRatio = 0,
                expectedNegativeRatio = 0,
            ),
            input(
                helpfulFeedbackCount = 3,
                notHelpfulFeedbackCount = 1,
                expectedPositiveRatio = 75,
                expectedNegativeRatio = 1,
            ),
            input(
                helpfulFeedbackCount = 2,
                notHelpfulFeedbackCount = 1,
                expectedPositiveRatio = 67,
                expectedNegativeRatio = 33,
            ),
            input(
                helpfulFeedbackCount = 78,
                notHelpfulFeedbackCount = 22,
                expectedPositiveRatio = 78,
                expectedNegativeRatio = 22,
            ),
        )

        private fun input(
            helpfulFeedbackCount: Int,
            notHelpfulFeedbackCount: Int,
            expectedPositiveRatio: Int,
            expectedNegativeRatio: Int,
        ) = arrayOf(helpfulFeedbackCount, notHelpfulFeedbackCount, expectedPositiveRatio, expectedNegativeRatio)
    }

    @Autowired
    private lateinit var useCase: FeedbackQagUseCase

    @MockBean
    private lateinit var feedbackQagRepository: GetFeedbackQagRepository

    @MockBean
    private lateinit var resultsCacheRepository: FeedbackResultsCacheRepository

    @MockBean
    private lateinit var userFeedbackCacheRepository: UserFeedbackQagCacheRepository

    @Test
    fun `getFeedbackResults - when has CachedFeedbackResults - should return result from cache`() {
        // Given
        val feedbackResults = mock(FeedbackResults::class.java)
        given(resultsCacheRepository.getFeedbackResults(qagId = "qagId"))
            .willReturn(FeedbackResultsCacheResult.CachedFeedbackResults(feedbackResults))

        // When
        val result = useCase.getFeedbackResults(qagId = "qagId")

        // Then
        assertThat(result).isEqualTo(feedbackResults)
        then(resultsCacheRepository).should(only()).getFeedbackResults(qagId = "qagId")
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagRepository).shouldHaveNoInteractions()
    }

    @MethodSource("getFeedbackResultsTestCases")
    @ParameterizedTest(name = "getFeedbackResults - when has {0} yes and {1} no - should return {2}% yes and {3}% no")
    fun `getFeedbackResults - when has FeedbackResultsCacheNotInitialized - should return expected result and store to cache`(
        helpfulFeedbackCount: Int,
        notHelpfulFeedbackCount: Int,
        expectedPositiveRatio: Int,
        expectedNegativeRatio: Int,
    ) {
        // Given
        given(resultsCacheRepository.getFeedbackResults(qagId = "qagId"))
            .willReturn(FeedbackResultsCacheResult.FeedbackResultsCacheNotInitialized)

        val feedbackList = (0 until helpfulFeedbackCount).map { mockFeedbackQag(isHelpful = true) } +
                (0 until notHelpfulFeedbackCount).map { mockFeedbackQag(isHelpful = false) }
        given(feedbackQagRepository.getFeedbackQagList(qagId = "qagId")).willReturn(feedbackList)

        // When
        val result = useCase.getFeedbackResults(qagId = "qagId")

        // Then
        val expectedResults = FeedbackResults(
            positiveRatio = expectedPositiveRatio,
            negativeRatio = expectedNegativeRatio,
            count = helpfulFeedbackCount + notHelpfulFeedbackCount,
        )
        assertThat(result).isEqualTo(expectedResults)
        then(resultsCacheRepository).should().getFeedbackResults(qagId = "qagId")
        then(resultsCacheRepository).should().initFeedbackResults(qagId = "qagId", results = expectedResults)
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagRepository).should(only()).getFeedbackQagList(qagId = "qagId")
    }


    @Test
    fun `getUserFeedbackQagIds - when has CachedUserFeedback - should return result from cache`() {
        // Given
        val userFeedbackQagIds = listOf("qagId")
        given(userFeedbackCacheRepository.getUserFeedbackQagIds(userId = "userId"))
            .willReturn(UserFeedbackQagCacheResult.CachedUserFeedback(userFeedbackQagIds = userFeedbackQagIds))

        // When
        val result = useCase.getUserFeedbackQagIds(userId = "userId")

        // Then
        assertThat(result).isEqualTo(userFeedbackQagIds)
        then(resultsCacheRepository).shouldHaveNoInteractions()
        then(userFeedbackCacheRepository).should(only()).getUserFeedbackQagIds(userId = "userId")
        then(feedbackQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getUserFeedbackQagIds - when has UserFeedbackCacheNotInitialized - should return result from repository and init cache`() {
        // Given
        given(userFeedbackCacheRepository.getUserFeedbackQagIds(userId = "userId"))
            .willReturn(UserFeedbackQagCacheResult.UserFeedbackCacheNotInitialized)
        val userFeedbackQagIds = listOf("qagId")
        given(feedbackQagRepository.getUserFeedbackQagIds(userId = "userId")).willReturn(userFeedbackQagIds)

        // When
        val result = useCase.getUserFeedbackQagIds(userId = "userId")

        // Then
        assertThat(result).isEqualTo(userFeedbackQagIds)
        then(resultsCacheRepository).shouldHaveNoInteractions()
        then(userFeedbackCacheRepository).should().getUserFeedbackQagIds(userId = "userId")
        then(userFeedbackCacheRepository).should()
            .initUserFeedbackQagIds(userId = "userId", qagIds = userFeedbackQagIds)
        then(userFeedbackCacheRepository).shouldHaveNoMoreInteractions()
        then(feedbackQagRepository).should(only()).getUserFeedbackQagIds(userId = "userId")
    }

    private fun mockFeedbackQag(isHelpful: Boolean) = mock(FeedbackQag::class.java).also {
        given(it.isHelpful).willReturn(isHelpful)
    }

}
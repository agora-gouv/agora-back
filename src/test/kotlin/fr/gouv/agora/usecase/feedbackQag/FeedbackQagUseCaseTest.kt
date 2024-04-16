package fr.gouv.agora.usecase.feedbackQag

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.domain.FeedbackResults
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheResult
import fr.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.only
import org.mockito.Mockito.reset
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FeedbackQagUseCaseTest {

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
                expectedNegativeRatio = 25,
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

    @InjectMocks
    lateinit var useCase: FeedbackQagUseCase

    @Mock
    lateinit var featureFlagsRepository: FeatureFlagsRepository

    @Mock
    lateinit var feedbackQagRepository: GetFeedbackQagRepository

    @Mock
    lateinit var resultsCacheRepository: FeedbackResultsCacheRepository

    @Mock
    lateinit var userFeedbackCacheRepository: UserFeedbackQagCacheRepository

    @BeforeEach
    fun setUp() {
        reset(featureFlagsRepository)
    }

    @Test
    fun `getFeedbackResults - when feature disabled - should return null`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(false)

        // When
        val result = useCase.getFeedbackResults(qagId = "qagId")

        // Then
        assertThat(result).isEqualTo(null)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        then(resultsCacheRepository).shouldHaveNoInteractions()
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getFeedbackResults - when feature enabled and has CachedFeedbackResults - should return result from cache`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)

        val feedbackResults = mock(FeedbackResults::class.java)
        given(resultsCacheRepository.getFeedbackResults(qagId = "qagId"))
            .willReturn(FeedbackResultsCacheResult.CachedFeedbackResults(feedbackResults))

        // When
        val result = useCase.getFeedbackResults(qagId = "qagId")

        // Then
        assertThat(result).isEqualTo(feedbackResults)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        then(resultsCacheRepository).should(only()).getFeedbackResults(qagId = "qagId")
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagRepository).shouldHaveNoInteractions()
    }

    @MethodSource("getFeedbackResultsTestCases")
    @ParameterizedTest(name = "getFeedbackResults - when has {0} yes and {1} no - should return {2}% yes and {3}% no")
    fun `getFeedbackResults - when feature enabled and has FeedbackResultsCacheNotInitialized - should return expected result and store to cache`(
        helpfulFeedbackCount: Int,
        notHelpfulFeedbackCount: Int,
        expectedPositiveRatio: Int,
        expectedNegativeRatio: Int,
    ) {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)

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
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        then(resultsCacheRepository).should().getFeedbackResults(qagId = "qagId")
        then(resultsCacheRepository).should().initFeedbackResults(qagId = "qagId", results = expectedResults)
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagRepository).should(only()).getFeedbackQagList(qagId = "qagId")
    }

    private fun mockFeedbackQag(isHelpful: Boolean) = mock(FeedbackQag::class.java).also {
        given(it.isHelpful).willReturn(isHelpful)
    }
}

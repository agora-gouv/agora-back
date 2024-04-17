package fr.gouv.agora.usecase.feedbackQag

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.domain.FeedbackResults
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.only
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class InsertFeedbackQagUseCaseTest {

    @InjectMocks
    private lateinit var useCase: InsertFeedbackQagUseCase

    @Mock
    private lateinit var repository: FeedbackQagRepository

    @Mock
    private lateinit var featureFlagsUseCase: FeatureFlagsUseCase

    @Mock
    private lateinit var feedbackQagUseCase: FeedbackQagUseCase

    @Mock
    private lateinit var userFeedbackCacheRepository: UserFeedbackQagCacheRepository

    @Mock
    private lateinit var feedbackResultsCacheRepository: FeedbackResultsCacheRepository

    private val feedbackQagInserting = FeedbackQagInserting(
        qagId = "qagId",
        userId = "userId",
        isHelpful = false,
    )

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag fails - should return failure`() {
        // Given
        given(repository.insertOrUpdateFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.FAILURE)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Failure)
        then(repository).should(only()).insertOrUpdateFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).shouldHaveNoInteractions()
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackResultsCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns success and feature disabled - should return FeedbackDisabled`() {
        // Given
        given(repository.insertOrUpdateFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(false)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.SuccessFeedbackDisabled)
        then(repository).should(only()).insertOrUpdateFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        then(userFeedbackCacheRepository).should(only()).addUserFeedbackQagId(userId = "userId", qagId = "qagId")
        then(feedbackResultsCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns success, feature enabled but return null feedbackResult - should update caches then return SuccessFeedbackDisabled`() {
        // Given
        given(repository.insertOrUpdateFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)
        given(feedbackQagUseCase.getFeedbackResults(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.SuccessFeedbackDisabled)
        then(repository).should(only()).insertOrUpdateFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        inOrder(userFeedbackCacheRepository, feedbackResultsCacheRepository, feedbackQagUseCase).also {
            then(userFeedbackCacheRepository).should(it).addUserFeedbackQagId(userId = "userId", qagId = "qagId")
            then(feedbackResultsCacheRepository).should(it).evictFeedbackResults(qagId = "qagId")
            then(feedbackQagUseCase).should(it).getFeedbackResults(qagId = "qagId")
            it.verifyNoMoreInteractions()
        }
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns success, feature enabled and return non-null feedbackResult - should update caches then return Success with new feedback results`() {
        // Given
        given(repository.insertOrUpdateFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)
        val feedbackResults = mock(FeedbackResults::class.java)
        given(feedbackQagUseCase.getFeedbackResults(qagId = "qagId")).willReturn(feedbackResults)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Success(feedbackResults))
        then(repository).should(only()).insertOrUpdateFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        inOrder(userFeedbackCacheRepository, feedbackResultsCacheRepository, feedbackQagUseCase).also {
            then(userFeedbackCacheRepository).should(it).addUserFeedbackQagId(userId = "userId", qagId = "qagId")
            then(feedbackResultsCacheRepository).should(it).evictFeedbackResults(qagId = "qagId")
            then(feedbackQagUseCase).should(it).getFeedbackResults(qagId = "qagId")
            it.verifyNoMoreInteractions()
        }
    }
}
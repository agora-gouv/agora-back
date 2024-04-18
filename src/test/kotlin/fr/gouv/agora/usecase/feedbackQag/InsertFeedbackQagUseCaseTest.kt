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
import org.junit.jupiter.api.Nested
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

    @Nested
    inner class InsertFeedbackTestCases {

        @Test
        fun `insertFeedbackQag - when insertFeedbackQag fails - should return failure`() {
            // Given
            given(repository.getFeedbackResponseForUser(qagId = "qagId", userId = "userId")).willReturn(null)
            given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.FAILURE)

            // When
            val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

            // Then
            assertThat(result).isEqualTo(InsertFeedbackQagResult.Failure)
            then(repository).should().getFeedbackResponseForUser(qagId = "qagId", userId = "userId")
            then(repository).should().insertFeedbackQag(feedbackQagInserting)
            then(repository).shouldHaveNoMoreInteractions()
            then(featureFlagsUseCase).shouldHaveNoInteractions()
            then(userFeedbackCacheRepository).shouldHaveNoInteractions()
            then(feedbackResultsCacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `insertFeedbackQag - when insertFeedbackQag returns success and feature disabled - should return FeedbackDisabled`() {
            // Given
            given(repository.getFeedbackResponseForUser(qagId = "qagId", userId = "userId")).willReturn(null)
            given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
            given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(false)

            // When
            val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

            // Then
            assertThat(result).isEqualTo(InsertFeedbackQagResult.SuccessFeedbackDisabled)
            then(repository).should().getFeedbackResponseForUser(qagId = "qagId", userId = "userId")
            then(repository).should().insertFeedbackQag(feedbackQagInserting)
            then(repository).shouldHaveNoMoreInteractions()
            then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
            then(userFeedbackCacheRepository).should(only()).initUserFeedbackResponse(
                userId = feedbackQagInserting.userId,
                qagId = feedbackQagInserting.qagId,
                userFeedbackResponse = null,
            )
            then(feedbackResultsCacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `insertFeedbackQag - when insertFeedbackQag returns success, feature enabled but return null feedbackResult - should update caches then return SuccessFeedbackDisabled`() {
            // Given
            given(repository.getFeedbackResponseForUser(qagId = "qagId", userId = "userId")).willReturn(null)
            given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
            given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)
            given(feedbackQagUseCase.getFeedbackResults(qagId = feedbackQagInserting.qagId)).willReturn(null)

            // When
            val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

            // Then
            assertThat(result).isEqualTo(InsertFeedbackQagResult.SuccessFeedbackDisabled)
            then(repository).should().getFeedbackResponseForUser(qagId = "qagId", userId = "userId")
            then(repository).should().insertFeedbackQag(feedbackQagInserting)
            then(repository).shouldHaveNoMoreInteractions()
            then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
            inOrder(userFeedbackCacheRepository, feedbackResultsCacheRepository, feedbackQagUseCase).also {
                then(userFeedbackCacheRepository).should(it).initUserFeedbackResponse(
                    userId = feedbackQagInserting.userId,
                    qagId = feedbackQagInserting.qagId,
                    userFeedbackResponse = null,
                )
                then(feedbackResultsCacheRepository).should(it).evictFeedbackResults(qagId = feedbackQagInserting.qagId)
                then(feedbackQagUseCase).should(it).getFeedbackResults(qagId = feedbackQagInserting.qagId)
                it.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `insertFeedbackQag - when insertFeedbackQag returns success, feature enabled and return non-null feedbackResult - should update caches then return Success with new feedback results`() {
            // Given
            given(repository.getFeedbackResponseForUser(qagId = "qagId", userId = "userId")).willReturn(null)
            given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
            given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)
            val feedbackResults = mock(FeedbackResults::class.java)
            given(feedbackQagUseCase.getFeedbackResults(qagId = feedbackQagInserting.qagId)).willReturn(feedbackResults)

            // When
            val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

            // Then
            assertThat(result).isEqualTo(InsertFeedbackQagResult.Success(feedbackResults))
            then(repository).should().getFeedbackResponseForUser(qagId = "qagId", userId = "userId")
            then(repository).should().insertFeedbackQag(feedbackQagInserting)
            then(repository).shouldHaveNoMoreInteractions()
            then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
            inOrder(userFeedbackCacheRepository, feedbackResultsCacheRepository, feedbackQagUseCase).also {
                then(userFeedbackCacheRepository).should(it).initUserFeedbackResponse(
                    userId = feedbackQagInserting.userId,
                    qagId = feedbackQagInserting.qagId,
                    userFeedbackResponse = null,
                )
                then(feedbackResultsCacheRepository).should(it).evictFeedbackResults(qagId = feedbackQagInserting.qagId)
                then(feedbackQagUseCase).should(it).getFeedbackResults(qagId = feedbackQagInserting.qagId)
                it.verifyNoMoreInteractions()
            }
        }

    }

    @Nested
    inner class UpdateFeedbackTestCases {

        @Test
        fun `insertFeedbackQag - when feedback already submitted - should update existing feedback`() {
            // Given
            given(repository.getFeedbackResponseForUser(qagId = "qagId", userId = "userId")).willReturn(true)
            given(repository.updateFeedbackQag(qagId = "qagId", userId = "userId", isHelpful = false))
                .willReturn(FeedbackQagResult.SUCCESS)
            given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(false)

            // When
            val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting.copy(isHelpful = false))

            // Then
            assertThat(result).isEqualTo(InsertFeedbackQagResult.SuccessFeedbackDisabled)
            then(repository).should().getFeedbackResponseForUser(qagId = "qagId", userId = "userId")
            then(repository).should().updateFeedbackQag(qagId = "qagId", userId = "userId", isHelpful = false)
            then(repository).shouldHaveNoMoreInteractions()
        }

    }

}
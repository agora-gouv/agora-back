package fr.gouv.agora.usecase.feedbackQag

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
internal class InsertFeedbackQagUseCaseTest {

    @Autowired
    private lateinit var useCase: InsertFeedbackQagUseCase

    @MockBean
    private lateinit var repository: FeedbackQagRepository

    @MockBean
    private lateinit var featureFlagsUseCase: FeatureFlagsUseCase

    @MockBean
    private lateinit var feedbackQagUseCase: FeedbackQagUseCase

    @MockBean
    private lateinit var userFeedbackCacheRepository: UserFeedbackQagCacheRepository

    @MockBean
    private lateinit var feedbackResultsCacheRepository: FeedbackResultsCacheRepository

    private val feedbackQagInserting = FeedbackQagInserting(
        qagId = "qagId",
        userId = "userId",
        isHelpful = false,
    )

    @Test
    fun `insertFeedbackQag - when feedback from userId with same qagId exists - should return failure`() {
        // Given
        given(feedbackQagUseCase.getUserFeedbackQagIds(userId = "userId")).willReturn(listOf("qagId"))

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Failure)
        then(repository).shouldHaveNoInteractions()
        then(featureFlagsUseCase).shouldHaveNoInteractions()
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackResultsCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).should(only()).getUserFeedbackQagIds(userId = "userId")
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag fails - should return failure`() {
        // Given
        given(feedbackQagUseCase.getUserFeedbackQagIds(userId = "userId")).willReturn(emptyList())
        given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.FAILURE)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Failure)
        then(repository).should(only()).insertFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).shouldHaveNoInteractions()
        then(userFeedbackCacheRepository).shouldHaveNoInteractions()
        then(feedbackResultsCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).should(only()).getUserFeedbackQagIds(userId = "userId")
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns success and feature disabled - should return FeedbackDisabled`() {
        // Given
        given(feedbackQagUseCase.getUserFeedbackQagIds(userId = "userId")).willReturn(emptyList())
        given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(false)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.SuccessFeedbackDisabled)
        then(repository).should(only()).insertFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        then(userFeedbackCacheRepository).should(only()).addUserFeedbackQagId(userId = "userId", qagId = "qagId")
        then(feedbackResultsCacheRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).should(only()).getUserFeedbackQagIds(userId = "userId")
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns success, feature enabled but return null feedbackResult - should update caches then return SuccessFeedbackDisabled`() {
        // Given
        given(feedbackQagUseCase.getUserFeedbackQagIds(userId = "userId")).willReturn(emptyList())
        given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)
        given(feedbackQagUseCase.getFeedbackResults(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.SuccessFeedbackDisabled)
        then(repository).should(only()).insertFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        inOrder(userFeedbackCacheRepository, feedbackResultsCacheRepository, feedbackQagUseCase).also {
            then(feedbackQagUseCase).should(it).getUserFeedbackQagIds(userId = "userId")
            then(userFeedbackCacheRepository).should(it).addUserFeedbackQagId(userId = "userId", qagId = "qagId")
            then(feedbackResultsCacheRepository).should(it).evictFeedbackResults(qagId = "qagId")
            then(feedbackQagUseCase).should(it).getFeedbackResults(qagId = "qagId")
            it.verifyNoMoreInteractions()
        }
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns success, feature enabled and return non-null feedbackResult - should update caches then return Success with new feedback results`() {
        // Given
        given(feedbackQagUseCase.getUserFeedbackQagIds(userId = "userId")).willReturn(emptyList())
        given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)
        val feedbackResults = mock(FeedbackResults::class.java)
        given(feedbackQagUseCase.getFeedbackResults(qagId = "qagId")).willReturn(feedbackResults)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Success(feedbackResults))
        then(repository).should(only()).insertFeedbackQag(feedbackQagInserting)
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        inOrder(userFeedbackCacheRepository, feedbackResultsCacheRepository, feedbackQagUseCase).also {
            then(feedbackQagUseCase).should(it).getUserFeedbackQagIds(userId = "userId")
            then(userFeedbackCacheRepository).should(it).addUserFeedbackQagId(userId = "userId", qagId = "qagId")
            then(feedbackResultsCacheRepository).should(it).evictFeedbackResults(qagId = "qagId")
            then(feedbackQagUseCase).should(it).getFeedbackResults(qagId = "qagId")
            it.verifyNoMoreInteractions()
        }
    }
}
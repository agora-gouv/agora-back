package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
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
    private lateinit var getFeedbackQagRepository: GetFeedbackQagRepository

    @MockBean
    private lateinit var featureFlagsUseCase: FeatureFlagsUseCase

    @MockBean
    private lateinit var mapper: FeedbackQagResultMapper

    private val feedbackQagInserting = FeedbackQagInserting(
        qagId = "qagId",
        userId = "userId",
        isHelpful = false,
    )

    private val feedbackQag = FeedbackQag(
        qagId = "qagId",
        userId = "userId",
        isHelpful = false,
    )

    @Test
    fun `insertFeedbackQag - when feedback exists - should return failure`() {
        // Given
        given(getFeedbackQagRepository.getFeedbackQagList(qagId = "qagId")).willReturn(listOf(feedbackQag))

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Failure)
        then(repository).shouldHaveNoInteractions()
        then(featureFlagsUseCase).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
        then(getFeedbackQagRepository).should(only()).getFeedbackQagList(qagId = "qagId")
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag fails - should return failure`() {
        // Given
        given(getFeedbackQagRepository.getFeedbackQagList(qagId = "qagId")).willReturn(emptyList())
        given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.FAILURE)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Failure)
        then(repository).should(only()).insertFeedbackQag(feedbackQagInserting)
        then(getFeedbackQagRepository).should(only()).getFeedbackQagList(qagId = "qagId")
        then(featureFlagsUseCase).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns succes and feature disabled - should return FeedbackDisabled`() {
        // Given
        given(getFeedbackQagRepository.getFeedbackQagList(qagId = "qagId")).willReturn(emptyList())
        given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(false)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.SuccessFeedbackDisabled)
        then(repository).should(only()).insertFeedbackQag(feedbackQagInserting)
        then(getFeedbackQagRepository).should(only()).getFeedbackQagList(qagId = "qagId")
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `insertFeedbackQag - when insertFeedbackQag returns succes and feature enabled - should return Success with feedback list`() {
        // Given
        given(getFeedbackQagRepository.getFeedbackQagList(qagId = "qagId")).willReturn(emptyList())
        given(repository.insertFeedbackQag(feedbackQagInserting)).willReturn(FeedbackQagResult.SUCCESS)
        given(featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)).willReturn(true)
        given(mapper.toFeedbackQag(feedbackQagInserting = feedbackQagInserting)).willReturn(feedbackQag)

        // When
        val result = useCase.insertFeedbackQag(feedbackQagInserting = feedbackQagInserting)

        // Then
        assertThat(result).isEqualTo(FeedbackQagListResult.Success(listOf(feedbackQag)))
        then(repository).should(only()).insertFeedbackQag(feedbackQagInserting)
        then(getFeedbackQagRepository).should(only()).getFeedbackQagList(qagId = "qagId")
        then(featureFlagsUseCase).should(only()).isFeatureEnabled(AgoraFeature.FeedbackResponseQag)
        then(mapper).should(only()).toFeedbackQag(feedbackQagInserting)
    }
}
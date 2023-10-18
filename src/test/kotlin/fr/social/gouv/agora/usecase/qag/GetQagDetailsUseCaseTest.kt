package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagDetails
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.QagWithUserData
import fr.social.gouv.agora.usecase.feedbackQag.FeedbackQagUseCase
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetQagDetailsUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagDetailsUseCase

    @MockBean
    private lateinit var qagDetailsAggregate: QagDetailsAggregate

    @MockBean
    private lateinit var supportQagRepository: GetSupportQagRepository

    @MockBean
    private lateinit var feedbackQagUseCase: FeedbackQagUseCase

    @Test
    fun `getQag - when qag is null - should return QagNotFound`() {
        // Given
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagNotFound)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status ARCHIVED - should return QagNotFound`() {
        // Given
        val qag = mockQag(status = QagStatus.ARCHIVED)
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagNotFound)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status MODERATED_REJECTED - should return QagNotFound`() {
        // Given
        val qag = mockQag(status = QagStatus.MODERATED_REJECTED)
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagRejectedStatus)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status OPEN but userId is not the same as QaG author - should return QagNotFound`() {
        // Given
        val qag = mockQag(status = QagStatus.OPEN, userId = "anotherUserId")
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagNotFound)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status OPEN and userId is the same as QaG author - should return Success`() {
        // Given
        val qag = mockQag(status = QagStatus.OPEN, userId = "userId")
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            QagResult.Success(
                QagWithUserData(
                    qagDetails = qag,
                    canShare = false,
                    canSupport = true,
                    canDelete = true,
                    isAuthor = true,
                    isSupportedByUser = false,
                    hasGivenFeedback = false,
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).should(only()).getUserSupportedQags(userId = "userId")
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status MODERATED_ACCEPTED and not supported by user - should return Success`() {
        // Given
        val qag = mockQag(status = QagStatus.MODERATED_ACCEPTED, userId = "anotherUserId")
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)
        given(supportQagRepository.getUserSupportedQags(userId = "userId")).willReturn(emptyList())

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            QagResult.Success(
                QagWithUserData(
                    qagDetails = qag,
                    canShare = true,
                    canSupport = true,
                    canDelete = false,
                    isAuthor = false,
                    isSupportedByUser = false,
                    hasGivenFeedback = false,
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).should(only()).getUserSupportedQags(userId = "userId")
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status MODERATED_ACCEPTED and supported by user - should return Success`() {
        // Given
        val qag = mockQag(status = QagStatus.MODERATED_ACCEPTED, userId = "userId")
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)
        given(supportQagRepository.getUserSupportedQags(userId = "userId")).willReturn(listOf("qagId"))

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            QagResult.Success(
                QagWithUserData(
                    qagDetails = qag,
                    canShare = true,
                    canSupport = true,
                    canDelete = true,
                    isAuthor = true,
                    isSupportedByUser = true,
                    hasGivenFeedback = false,
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).should(only()).getUserSupportedQags(userId = "userId")
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status SELECTED_FOR_RESPONSE and has not given feedback - should return Success`() {
        // Given
        val qag = mockQag(status = QagStatus.SELECTED_FOR_RESPONSE, userId = "anotherUserId")
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)
        given(feedbackQagUseCase.getUserFeedbackQagIds(userId = "userId")).willReturn(emptyList())

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            QagResult.Success(
                QagWithUserData(
                    qagDetails = qag,
                    canShare = true,
                    canSupport = false,
                    canDelete = false,
                    isAuthor = false,
                    isSupportedByUser = true,
                    hasGivenFeedback = false,
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).should(only()).getUserFeedbackQagIds(userId = "userId")
    }

    @Test
    fun `getQag - when status SELECTED_FOR_RESPONSE and has given feedback - should return Success`() {
        // Given
        val qag = mockQag(status = QagStatus.SELECTED_FOR_RESPONSE, userId = "userId")
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)
        given(feedbackQagUseCase.getUserFeedbackQagIds(userId = "userId")).willReturn(listOf("qagId"))

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            QagResult.Success(
                QagWithUserData(
                    qagDetails = qag,
                    canShare = true,
                    canSupport = false,
                    canDelete = false,
                    isAuthor = true,
                    isSupportedByUser = true,
                    hasGivenFeedback = true,
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagRepository).shouldHaveNoInteractions()
        then(feedbackQagUseCase).should(only()).getUserFeedbackQagIds(userId = "userId")
    }

    private fun mockQag(
        status: QagStatus,
        userId: String = "userId",
    ): QagDetails {
        return mock(QagDetails::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.status).willReturn(status)
            given(it.userId).willReturn(userId)
        }
    }

}
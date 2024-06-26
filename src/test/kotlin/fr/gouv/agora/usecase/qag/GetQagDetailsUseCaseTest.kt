package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.QagWithUserData
import fr.gouv.agora.usecase.feedbackQag.FeedbackQagUseCase
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.only
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GetQagDetailsUseCaseTest {

    @InjectMocks
    lateinit var useCase: GetQagDetailsUseCase

    @Mock
    lateinit var qagDetailsAggregate: QagDetailsAggregate

    @Mock
    lateinit var supportQagUseCase: SupportQagUseCase

    @Mock
    lateinit var feedbackQagUseCase: FeedbackQagUseCase

    @Mock
    lateinit var mapper: QagDetailsMapper

    @Test
    fun `getQag - when qag is null - should return QagNotFound`() {
        // Given
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagNotFound)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagUseCase).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status ARCHIVED - should return QagNotFound`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.status).willReturn(QagStatus.ARCHIVED)
        }

        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagNotFound)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagUseCase).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status MODERATED_REJECTED - should return QagNotFound`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.status).willReturn(QagStatus.MODERATED_REJECTED)
        }

        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagRejectedStatus)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagUseCase).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status OPEN but userId is not the same as QaG author - should return QagNotFound`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.status).willReturn(QagStatus.OPEN)
            given(details.userId).willReturn("anotherUserId")
        }

        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(QagResult.QagNotFound)
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagUseCase).shouldHaveNoInteractions()
        then(feedbackQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status OPEN and userId is the same as QaG author - should return Success`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.status).willReturn(QagStatus.OPEN)
            given(details.userId).willReturn("userId")
        }

        given(feedbackQagUseCase.getFeedbackForQagAndUser(qagId = "qagId", userId = "userId"))
            .willReturn(null)
        given(mapper.toQagWithoutFeedbackResults(qag)).willReturn(qag)
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
                    isHelpful = null
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagUseCase).should(only()).getUserSupportedQagIds(userId = "userId")
        then(feedbackQagUseCase).should(only()).getFeedbackForQagAndUser("qagId", "userId")
    }

    @Test
    fun `getQag - when status MODERATED_ACCEPTED and not supported by user - should return Success`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.status).willReturn(QagStatus.MODERATED_ACCEPTED)
            given(details.userId).willReturn("anotherUserId")
        }

        given(feedbackQagUseCase.getFeedbackForQagAndUser(qagId = "qagId", userId = "userId"))
            .willReturn(null)
        given(mapper.toQagWithoutFeedbackResults(qag)).willReturn(qag)
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)
        given(supportQagUseCase.getUserSupportedQagIds(userId = "userId")).willReturn(emptyList())

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
                    isHelpful = null
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagUseCase).should(only()).getUserSupportedQagIds(userId = "userId")
        then(feedbackQagUseCase).should(only()).getFeedbackForQagAndUser("qagId", "userId")
    }

    @Test
    fun `getQag - when status MODERATED_ACCEPTED and supported by user - should return Success`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.id).willReturn("qagId")
            given(details.status).willReturn(QagStatus.MODERATED_ACCEPTED)
            given(details.userId).willReturn("userId")
        }

        given(mapper.toQagWithoutFeedbackResults(qag)).willReturn(qag)
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)
        given(supportQagUseCase.getUserSupportedQagIds(userId = "userId")).willReturn(listOf("qagId"))
        given(feedbackQagUseCase.getFeedbackForQagAndUser(qagId = "qagId", userId = "userId"))
            .willReturn(null)

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
                    isHelpful = null
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(supportQagUseCase).should(only()).getUserSupportedQagIds(userId = "userId")
        then(feedbackQagUseCase).should(only()).getFeedbackForQagAndUser("qagId", "userId")
    }

    @Test
    fun `getQag - when status SELECTED_FOR_RESPONSE and has not given feedback - should remove feedbackResults from qag and return Success`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.status).willReturn(QagStatus.SELECTED_FOR_RESPONSE)
            given(details.userId).willReturn("anotherUserId")
        }

        given(mapper.toQagWithoutFeedbackResults(qag)).willReturn(qag)
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

        val qagWithoutFeedbackResults = mock(QagDetails::class.java)
        given(mapper.toQagWithoutFeedbackResults(qag)).willReturn(qagWithoutFeedbackResults)

        given(feedbackQagUseCase.getFeedbackForQagAndUser(qagId = "qagId", userId = "userId"))
            .willReturn(null)

        // When
        val result = useCase.getQagDetails(qagId = "qagId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            QagResult.Success(
                QagWithUserData(
                    qagDetails = qagWithoutFeedbackResults,
                    canShare = true,
                    canSupport = false,
                    canDelete = false,
                    isAuthor = false,
                    isSupportedByUser = true,
                    isHelpful = null
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(feedbackQagUseCase).should(only()).getFeedbackForQagAndUser("qagId", "userId")
        then(supportQagUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when status SELECTED_FOR_RESPONSE and has given feedback - should return Success`() {
        // Given
        val qag = mock(QagDetails::class.java).also { details ->
            given(details.status).willReturn(QagStatus.SELECTED_FOR_RESPONSE)
            given(details.userId).willReturn("userId")
        }

        given(feedbackQagUseCase.getFeedbackForQagAndUser(qagId = "qagId", userId = "userId")).willReturn(false)
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qag)

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
                    isHelpful = false
                )
            )
        )
        then(qagDetailsAggregate).should(only()).getQag(qagId = "qagId")
        then(feedbackQagUseCase).should(only()).getFeedbackForQagAndUser("qagId", "userId")
        then(supportQagUseCase).shouldHaveNoInteractions()
    }
}
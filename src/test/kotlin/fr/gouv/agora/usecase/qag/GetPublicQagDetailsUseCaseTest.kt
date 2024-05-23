package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.domain.QagStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GetPublicQagDetailsUseCaseTest {

    @InjectMocks
    private lateinit var useCase: GetPublicQagDetailsUseCase

    @Mock
    lateinit var qagDetailsAggregate: QagDetailsAggregate

    @Test
    fun `getQagDetails - when has no Qag - should return null`() {
        // Given
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.getQagDetails(qagId = "qagId")

        // Then
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `getQagDetails - when has Qag which status is not MODERATED_ACCEPTED or SELECTED_FOR_RESPONSE - should return null`() {
        // Given
        val qagDetails = mock(QagDetails::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qagDetails)

        // When
        val result = useCase.getQagDetails(qagId = "qagId")

        // Then
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `getQagDetails - when has Qag which status is MODERATED_ACCEPTED - should return Qag`() {
        // Given
        val qagDetails = mock(QagDetails::class.java).also {
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qagDetails)

        // When
        val result = useCase.getQagDetails(qagId = "qagId")

        // Then
        assertThat(result).isEqualTo(qagDetails)
    }

    @Test
    fun `getQagDetails - when has Qag which status is SELECTED_FOR_RESPONSE - should return Qag`() {
        // Given
        val qagDetails = mock(QagDetails::class.java).also {
            given(it.status).willReturn(QagStatus.SELECTED_FOR_RESPONSE)
        }
        given(qagDetailsAggregate.getQag(qagId = "qagId")).willReturn(qagDetails)

        // When
        val result = useCase.getQagDetails(qagId = "qagId")

        // Then
        assertThat(result).isEqualTo(qagDetails)
    }

}
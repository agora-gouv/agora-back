package fr.gouv.agora.usecase.qagSelection

import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagUpdateResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class UpdateWinningQagsUseCaseTest {

    @InjectMocks
    private lateinit var useCase: UpdateWinningQagsUseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Test
    fun `execute - when qagID exists - should return success with updated qag`() {
        // Given
        val qagId = "qag-id-1"
        val updatedQagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.status).willReturn(QagStatus.SELECTED_FOR_RESPONSE)
        }
        given(qagInfoRepository.selectQagForResponse(qagId)).willReturn(
            QagUpdateResult.Success(updatedQagInfo)
        )

        // When
        val result = useCase.execute(listOf(qagId))

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0]).isInstanceOf(QagUpdateResult.Success::class.java)
        val successResult = result[0] as QagUpdateResult.Success
        assertThat(successResult.updatedQagInfo.id).isEqualTo(qagId)
        assertThat(successResult.updatedQagInfo.status).isEqualTo(QagStatus.SELECTED_FOR_RESPONSE)
        then(qagInfoRepository).should(only()).selectQagForResponse(qagId)
    }

    @Test
    fun `execute - when qagID does not exist - should return failure`() {
        // Given
        val qagId = "non-existent-qag-id"
        given(qagInfoRepository.selectQagForResponse(qagId)).willReturn(QagUpdateResult.Failure)

        // When
        val result = useCase.execute(listOf(qagId))

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0]).isInstanceOf(QagUpdateResult.Failure::class.java)
        then(qagInfoRepository).should(only()).selectQagForResponse(qagId)
    }

    @Test
    fun `execute - when update returns an error - should return failure`() {
        // Given
        val qagId = "error-qag-id"
        given(qagInfoRepository.selectQagForResponse(qagId)).willReturn(QagUpdateResult.Failure)

        // When
        val result = useCase.execute(listOf(qagId))

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0]).isInstanceOf(QagUpdateResult.Failure::class.java)
        then(qagInfoRepository).should(only()).selectQagForResponse(qagId)
    }

    @Test
    fun `execute - when multiple qagIDs - should process all and return mixed results`() {
        // Given
        val qagId1 = "qag-id-1"
        val qagId2 = "qag-id-2"
        val qagId3 = "non-existent-qag-id"

        val updatedQagInfo1 = mock(QagInfo::class.java)
        val updatedQagInfo2 = mock(QagInfo::class.java)

        given(qagInfoRepository.selectQagForResponse(qagId1)).willReturn(
            QagUpdateResult.Success(updatedQagInfo1)
        )
        given(qagInfoRepository.selectQagForResponse(qagId2)).willReturn(
            QagUpdateResult.Success(updatedQagInfo2)
        )
        given(qagInfoRepository.selectQagForResponse(qagId3)).willReturn(QagUpdateResult.Failure)

        // When
        val result = useCase.execute(listOf(qagId1, qagId2, qagId3))

        // Then
        assertThat(result).hasSize(3)
        assertThat(result[0]).isInstanceOf(QagUpdateResult.Success::class.java)
        assertThat(result[1]).isInstanceOf(QagUpdateResult.Success::class.java)
        assertThat(result[2]).isInstanceOf(QagUpdateResult.Failure::class.java)
        then(qagInfoRepository).should().selectQagForResponse(qagId1)
        then(qagInfoRepository).should().selectQagForResponse(qagId2)
        then(qagInfoRepository).should().selectQagForResponse(qagId3)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `execute - when empty list - should return empty list`() {
        // Given
        val emptyList = emptyList<String>()

        // When
        val result = useCase.execute(emptyList)

        // Then
        assertThat(result).isEmpty()
        then(qagInfoRepository).shouldHaveNoInteractions()
    }
}

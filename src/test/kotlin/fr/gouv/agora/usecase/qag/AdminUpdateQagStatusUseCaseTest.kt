package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagUpdateResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Date

@ExtendWith(MockitoExtension::class)
class AdminUpdateQagStatusUseCaseTest {

    @InjectMocks
    private lateinit var useCase: AdminUpdateQagStatusUseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    private val qagId = "qag-uuid-1234"
    private val qagInfo = QagInfo(
        id = qagId,
        thematiqueId = "thematique-uuid",
        title = "Ma question",
        description = "Description",
        date = Date(),
        status = QagStatus.OPEN,
        username = "Utilisateur",
        userId = "user-uuid",
    )

    @Nested
    inner class `updateQagStatus - when qag not found - should return NotFound` {

        @Test
        fun `updateQagStatus - when getQagInfo returns null - should return NotFound`() {
            // Given
            given(qagInfoRepository.getQagInfo(qagId)).willReturn(null)

            // When
            val result = useCase.updateQagStatus(qagId = qagId, newStatus = QagStatus.MODERATED_ACCEPTED)

            // Then
            assertThat(result).isEqualTo(AdminUpdateQagStatusResult.NotFound)
            then(qagInfoRepository).should().getQagInfo(qagId)
            then(qagInfoRepository).shouldHaveNoMoreInteractions()
        }
    }

    @Nested
    inner class `updateQagStatus - when qag found and update succeeds - should return Success` {

        @Test
        fun `updateQagStatus - when update to MODERATED_ACCEPTED succeeds - should return Success`() {
            // Given
            given(qagInfoRepository.getQagInfo(qagId)).willReturn(qagInfo)
            given(qagInfoRepository.updateQagStatus(qagId = qagId, newQagStatus = QagStatus.MODERATED_ACCEPTED))
                .willReturn(QagUpdateResult.Success(qagInfo.copy(status = QagStatus.MODERATED_ACCEPTED)))

            // When
            val result = useCase.updateQagStatus(qagId = qagId, newStatus = QagStatus.MODERATED_ACCEPTED)

            // Then
            assertThat(result).isEqualTo(AdminUpdateQagStatusResult.Success)
        }

        @Test
        fun `updateQagStatus - when update to MODERATED_REJECTED succeeds - should return Success`() {
            // Given
            given(qagInfoRepository.getQagInfo(qagId)).willReturn(qagInfo)
            given(qagInfoRepository.updateQagStatus(qagId = qagId, newQagStatus = QagStatus.MODERATED_REJECTED))
                .willReturn(QagUpdateResult.Success(qagInfo.copy(status = QagStatus.MODERATED_REJECTED)))

            // When
            val result = useCase.updateQagStatus(qagId = qagId, newStatus = QagStatus.MODERATED_REJECTED)

            // Then
            assertThat(result).isEqualTo(AdminUpdateQagStatusResult.Success)
        }

        @Test
        fun `updateQagStatus - when update to SELECTED_FOR_RESPONSE succeeds - should return Success`() {
            // Given
            given(qagInfoRepository.getQagInfo(qagId)).willReturn(qagInfo)
            given(qagInfoRepository.updateQagStatus(qagId = qagId, newQagStatus = QagStatus.SELECTED_FOR_RESPONSE))
                .willReturn(QagUpdateResult.Success(qagInfo.copy(status = QagStatus.SELECTED_FOR_RESPONSE)))

            // When
            val result = useCase.updateQagStatus(qagId = qagId, newStatus = QagStatus.SELECTED_FOR_RESPONSE)

            // Then
            assertThat(result).isEqualTo(AdminUpdateQagStatusResult.Success)
        }

        @Test
        fun `updateQagStatus - when update to ARCHIVED succeeds - should return Success`() {
            // Given
            given(qagInfoRepository.getQagInfo(qagId)).willReturn(qagInfo)
            given(qagInfoRepository.updateQagStatus(qagId = qagId, newQagStatus = QagStatus.ARCHIVED))
                .willReturn(QagUpdateResult.Success(qagInfo.copy(status = QagStatus.ARCHIVED)))

            // When
            val result = useCase.updateQagStatus(qagId = qagId, newStatus = QagStatus.ARCHIVED)

            // Then
            assertThat(result).isEqualTo(AdminUpdateQagStatusResult.Success)
        }
    }

    @Nested
    inner class `updateQagStatus - when qag found but update fails - should return Failure` {

        @Test
        fun `updateQagStatus - when updateQagStatus returns Failure - should return Failure`() {
            // Given
            given(qagInfoRepository.getQagInfo(qagId)).willReturn(qagInfo)
            given(qagInfoRepository.updateQagStatus(qagId = qagId, newQagStatus = QagStatus.MODERATED_ACCEPTED))
                .willReturn(QagUpdateResult.Failure)

            // When
            val result = useCase.updateQagStatus(qagId = qagId, newStatus = QagStatus.MODERATED_ACCEPTED)

            // Then
            assertThat(result).isEqualTo(AdminUpdateQagStatusResult.Failure)
        }
    }
}

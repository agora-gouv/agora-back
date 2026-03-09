package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.DataAccessException

@ExtendWith(MockitoExtension::class)
internal class ArchiveAllModeratedQagsUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ArchiveAllModeratedQagsUseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Test
    fun `archiveAllModeratedQags - when has multiple moderated qags - should archive all of them`() {
        // Given
        given(qagInfoRepository.archiveAllModeratedAcceptedQags()).willReturn(3)

        // When
        val result = useCase.archiveAllModeratedQags()

        // Then
        assertThat(result.archivedCount).isEqualTo(3)
        then(qagInfoRepository).should(only()).archiveAllModeratedAcceptedQags()
    }

    @Test
    fun `archiveAllModeratedQags - when has no moderated qags - should return zero archived`() {
        // Given
        given(qagInfoRepository.archiveAllModeratedAcceptedQags()).willReturn(0)

        // When
        val result = useCase.archiveAllModeratedQags()

        // Then
        assertThat(result.archivedCount).isEqualTo(0)
        then(qagInfoRepository).should(only()).archiveAllModeratedAcceptedQags()
    }

    @Test
    fun `archiveAllModeratedQags - when database exception occurs - should propagate exception`() {
        // Given
        given(qagInfoRepository.archiveAllModeratedAcceptedQags())
            .willThrow(object : DataAccessException("Database connection failed") {})

        // When/Then
        assertThatThrownBy { useCase.archiveAllModeratedQags() }
            .isInstanceOf(DataAccessException::class.java)
            .hasMessageContaining("Database connection failed")
    }
}

package fr.gouv.agora.infrastructure.consultationResults.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.infrastructure.consultationAggregate.dto.ConsultationResultAggregatedDTO
import fr.gouv.agora.infrastructure.consultationAggregate.repository.ConsultationResultAggregatedDatabaseRepository
import fr.gouv.agora.infrastructure.consultationAggregate.repository.ConsultationResultAggregatedMapper
import fr.gouv.agora.infrastructure.consultationAggregate.repository.ConsultationResultAggregatedRepositoryImpl
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationAggregatedInsertResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ConsultationResultAggregatedRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: ConsultationResultAggregatedRepositoryImpl

    @Mock
    private lateinit var databaseRepository: ConsultationResultAggregatedDatabaseRepository

    @Mock
    private lateinit var mapper: ConsultationResultAggregatedMapper

    @Test
    fun `insertConsultationResultAggregated - when mapper does not return dto - should do nothing`() {
        // Given
        val consultationResultAggregatedInserting = mock(ConsultationResultAggregatedInserting::class.java)
        given(mapper.toDto(consultationResultAggregatedInserting)).willReturn(null)

        // When
        val result = repository.insertConsultationResultAggregated(listOf(consultationResultAggregatedInserting))

        // Then
        assertThat(result).isEqualTo(ConsultationAggregatedInsertResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDto(consultationResultAggregatedInserting)
    }

    @Test
    fun `insertConsultationResultAggregated - when mapper returns dto - should add to database`() {
        // Given
        val consultationResultAggregatedInserting = mock(ConsultationResultAggregatedInserting::class.java)
        val consultationResultAggregatedDTO = mock(ConsultationResultAggregatedDTO::class.java)
        given(mapper.toDto(consultationResultAggregatedInserting)).willReturn(consultationResultAggregatedDTO)

        // When
        val result = repository.insertConsultationResultAggregated(listOf(consultationResultAggregatedInserting))

        // Then
        assertThat(result).isEqualTo(ConsultationAggregatedInsertResult.SUCCESS)
        then(databaseRepository).should(only()).saveAll(listOf(consultationResultAggregatedDTO))
        then(mapper).should().toDto(consultationResultAggregatedInserting)
        then(mapper).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `getConsultationResultAggregated - when invalid consultationId - should returns emptyList`() {
        // Given
        val consultationId = "invalid UUID"

        // When
        val result = repository.getConsultationResultAggregated(consultationId)

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationResultAggregated>())
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResultAggregated - when valid consultationId and database returns emptyList - should returns emptyList`() {
        // Given
        val consultationUUID = UUID.randomUUID()
        given(databaseRepository.getConsultationResultByConsultation(consultationUUID)).willReturn(emptyList())

        // When
        val result = repository.getConsultationResultAggregated(consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationResultAggregated>())
        then(databaseRepository).should(only()).getConsultationResultByConsultation(consultationUUID)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResultAggregated - when valid consultationId and database returns DTO - should returns mapped object`() {
        // Given
        val consultationUUID = UUID.randomUUID()
        val consultationResultAggregated = mock(ConsultationResultAggregated::class.java)
        val consultationResultAggregatedDTO = mock(ConsultationResultAggregatedDTO::class.java)
        given(databaseRepository.getConsultationResultByConsultation(consultationUUID)).willReturn(
            listOf(
                consultationResultAggregatedDTO
            )
        )
        given(mapper.toDomain(consultationResultAggregatedDTO)).willReturn(consultationResultAggregated)

        // When
        val result = repository.getConsultationResultAggregated(consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(listOf(consultationResultAggregated))
        then(databaseRepository).should(only()).getConsultationResultByConsultation(consultationUUID)
        then(mapper).should().toDomain(consultationResultAggregatedDTO)
        then(mapper).shouldHaveNoMoreInteractions()
    }

}
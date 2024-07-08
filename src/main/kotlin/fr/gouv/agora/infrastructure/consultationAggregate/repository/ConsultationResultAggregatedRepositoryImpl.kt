package fr.gouv.agora.infrastructure.consultationAggregate.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationAggregatedInsertResult
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationResultAggregatedRepositoryImpl(
    private val databaseRepository: ConsultationResultAggregatedDatabaseRepository,
    private val mapper: ConsultationResultAggregatedMapper,
) : ConsultationResultAggregatedRepository {

    override fun getAggregatedResultsConsultationIds(): List<String> {
        return databaseRepository.getAggregatedResultsConsultationIds()
    }

    override fun insertConsultationResultAggregated(consultationResults: List<ConsultationResultAggregatedInserting>): ConsultationAggregatedInsertResult {
        return consultationResults
            .mapNotNull { consultationResult -> mapper.toDto(consultationResult) }
            .takeIf { consultationResultDTOList -> consultationResultDTOList.isNotEmpty() }
            ?.also { databaseRepository.saveAll(it) }
            ?.let { ConsultationAggregatedInsertResult.SUCCESS }
            ?: ConsultationAggregatedInsertResult.FAILURE
    }

    override fun getConsultationResultAggregated(consultationId: String): List<ConsultationResultAggregated> {
        return databaseRepository.getConsultationResultByConsultation(consultationId)
            .map(mapper::toDomain)
    }
}

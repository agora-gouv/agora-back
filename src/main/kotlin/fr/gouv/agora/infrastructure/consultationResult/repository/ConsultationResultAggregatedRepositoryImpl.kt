package fr.gouv.agora.infrastructure.consultationResult.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.reponseConsultation.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.ConsultationAggregatedInsertResult
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationResultAggregatedRepositoryImpl(
    private val databaseRepository: ConsultationResultAggregatedDatabaseRepository,
    private val mapper: ConsultationResultAggregatedMapper,
) : ConsultationResultAggregatedRepository {

    override fun insertConsultationResultAggregated(consultationResults: List<ConsultationResultAggregatedInserting>): ConsultationAggregatedInsertResult {
        return consultationResults
            .mapNotNull { consultationResult -> mapper.toDto(consultationResult) }
            .takeIf { consultationResultDTOList -> consultationResultDTOList.isNotEmpty() }
            ?.also { databaseRepository.saveAll(it) }
            ?.let { ConsultationAggregatedInsertResult.SUCCESS }
            ?: ConsultationAggregatedInsertResult.FAILURE
    }

    override fun getConsultationResultAggregated(consultationId: String): List<ConsultationResultAggregated> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getConsultationResultByConsultation(consultationUUID).map(mapper::toDomain)
        } ?: emptyList()
    }
}
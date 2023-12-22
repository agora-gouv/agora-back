package fr.gouv.agora.infrastructure.consultationResult.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.reponseConsultation.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.InsertResult
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class ConsultationResultAggregatedRepositoryImpl(
    private val databaseRepository: ConsultationResultAggregatedDatabaseRepository,
    private val mapper: ConsultationResultAggregatedMapper,
) : ConsultationResultAggregatedRepository {

    override fun insertConsultationResultAggregated(consultationResults: List<ConsultationResultAggregatedInserting>): InsertResult {
        return consultationResults
            .mapNotNull { consultationResult -> mapper.toDto(consultationResult) }
            .takeIf { consultationResultDTOList -> consultationResultDTOList.isNotEmpty() }
            ?.also { databaseRepository.saveAll(it) }
            ?.let { InsertResult.INSERT_SUCCESS }
            ?: InsertResult.INSERT_FAILURE
    }

    override fun getConsultationResultAggregated(consultationId: String): List<ConsultationResultAggregated> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getConsultationResultByConsultation(consultationUUID).map(mapper::toDomain)
        } ?: emptyList()
    }
}
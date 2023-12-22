package fr.gouv.agora.usecase.reponseConsultation.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting

interface ConsultationResultAggregatedRepository {
    fun insertConsultationResultAggregated(consultationResults: List<ConsultationResultAggregatedInserting>): InsertResult
    fun getConsultationResultAggregated(consultationId: String): List<ConsultationResultAggregated>
}

enum class InsertResult {
    INSERT_SUCCESS, INSERT_FAILURE
}
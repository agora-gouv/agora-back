package fr.gouv.agora.usecase.reponseConsultation.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting

interface ConsultationResultAggregatedRepository {
    fun getAggregatedResultsConsultationIds(): List<String>
    fun insertConsultationResultAggregated(consultationResults: List<ConsultationResultAggregatedInserting>): ConsultationAggregatedInsertResult
    fun getConsultationResultAggregated(consultationId: String): List<ConsultationResultAggregated>
}

enum class ConsultationAggregatedInsertResult {
    SUCCESS, FAILURE
}
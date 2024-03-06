package fr.gouv.agora.usecase.consultationResults.repository

import fr.gouv.agora.domain.ConsultationResults

interface ConsultationResultsCacheRepository {
    fun getConsultationResults(consultationId: String): ConsultationResultsCacheResult
    fun initConsultationResults(consultationId: String, results: ConsultationResults)
    fun initConsultationResultsNotFound(consultationId: String)
    fun evictConsultationResultsCache(consultationId: String)
}

sealed class ConsultationResultsCacheResult {
    data class CachedConsultationResults(val results: ConsultationResults) : ConsultationResultsCacheResult()
    object ConsultationResultsNotFound : ConsultationResultsCacheResult()
    object ConsultationResultsCacheNotInitialized : ConsultationResultsCacheResult()
}
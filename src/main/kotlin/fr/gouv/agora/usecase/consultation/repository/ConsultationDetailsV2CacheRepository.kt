package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationDetailsV2

interface ConsultationDetailsV2CacheRepository {
    fun getUnansweredUsersConsultationDetails(consultationId: String): ConsultationUpdateCacheResult
    fun initUnansweredUsersConsultationDetails(consultationId: String, details: ConsultationDetailsV2?)

    fun getLastConsultationDetails(consultationId: String): ConsultationUpdateCacheResult
    fun initLastConsultationDetails(consultationId: String, details: ConsultationDetailsV2?)

    fun getConsultationDetails(consultationId: String, consultationUpdateId: String): ConsultationUpdateCacheResult
    fun initConsultationDetails(
        consultationId: String,
        consultationUpdateId: String,
        details: ConsultationDetailsV2?,
    )

    fun getParticipantCount(consultationId: String): Int?
    fun initParticipantCount(consultationId: String, participantCount: Int)

    fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean?
    fun initHasAnsweredConsultation(consultationId: String, userId: String, hasAnswered: Boolean)
    fun evictHasAnsweredConsultation(consultationId: String, userId: String)
}

sealed class ConsultationUpdateCacheResult {
    data class CachedConsultationsDetails(val details: ConsultationDetailsV2?) : ConsultationUpdateCacheResult()
    object ConsultationUpdateNotFound : ConsultationUpdateCacheResult()
    object CacheNotInitialized : ConsultationUpdateCacheResult()
}

package fr.gouv.agora.usecase.reponseConsultation.repository

import fr.gouv.agora.domain.*

interface GetConsultationResponseRepository {
    @Deprecated(message = "Should use getConsultationResponsesCount instead for better performances")
    fun getConsultationResponses(consultationId: String): List<ReponseConsultation>
    fun getParticipantCount(consultationId: String): Int
    fun getConsultationResponsesCount(consultationId: String): List<ResponseConsultationCount>
    fun getParticipantDemographicInfo(consultationId: String): DemographicInfoCount
    fun getParticipantDemographicInfoByChoices(consultationId: String): DemographicInfoCountByChoices
    fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean
    fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean>
    fun getUsersAnsweredConsultation(consultationId: String): List<String>
}
package fr.gouv.agora.usecase.reponseConsultation.repository

import fr.gouv.agora.domain.DemographicInfoCount
import fr.gouv.agora.domain.ReponseConsultation
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.domain.ResponseConsultationCountWithDemographicInfo

interface GetConsultationResponseRepository {
    @Deprecated(message = "Should use getConsultationResponsesCount instead for better performances")
    fun getConsultationResponses(consultationId: String): List<ReponseConsultation>
    fun getParticipantDemographicInfo(consultationId: String): DemographicInfoCount
    fun getParticipantCount(consultationId: String): Int
    fun getConsultationResponsesCount(consultationId: String): List<ResponseConsultationCount>
    fun getConsultationResponsesCountWithDemographicInfo(consultationId: String): List<ResponseConsultationCountWithDemographicInfo>
    fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean
    fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean>
    fun getUsersAnsweredConsultation(consultationId: String): List<String>
}
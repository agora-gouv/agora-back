package fr.gouv.agora.usecase.consultationResponse.repository

import fr.gouv.agora.domain.DemographicInfoCount
import fr.gouv.agora.domain.DemographicInfoCountByChoices
import fr.gouv.agora.domain.ReponseConsultation
import fr.gouv.agora.domain.ResponseConsultationCount

interface GetConsultationResponseRepository {
    @Deprecated(message = "Should use getConsultationResponsesCount instead for better performances")
    fun getConsultationResponses(consultationId: String): List<ReponseConsultation>
    fun getConsultationResponsesCount(consultationId: String): List<ResponseConsultationCount>
    fun deleteConsultationResponses(consultationId: String)
    fun getParticipantDemographicInfo(consultationId: String): DemographicInfoCount
    fun getParticipantDemographicInfoByChoices(questionId: String): DemographicInfoCountByChoices
    fun deleteUserConsultationResponses(userIDs: List<String>)
}
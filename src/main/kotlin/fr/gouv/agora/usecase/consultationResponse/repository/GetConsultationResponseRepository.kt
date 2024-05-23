package fr.gouv.agora.usecase.consultationResponse.repository

import fr.gouv.agora.domain.DemographicInfoCount
import fr.gouv.agora.domain.DemographicInfoCountByChoices
import fr.gouv.agora.domain.ResponseConsultationCount

interface GetConsultationResponseRepository {
    fun getConsultationResponsesCount(consultationId: String): List<ResponseConsultationCount>
    fun deleteConsultationResponses(consultationId: String)
    fun getParticipantDemographicInfo(consultationId: String): DemographicInfoCount
    fun getParticipantDemographicInfoByChoices(questionId: String): DemographicInfoCountByChoices
    fun deleteUserConsultationResponses(userIDs: List<String>)
}
package fr.social.gouv.agora.usecase.consultationUpdate.repository

import fr.social.gouv.agora.domain.ConsultationUpdate

interface ConsultationUpdateRepository {
    fun getConsultationUpdates(consultationIDs: List<String>): List<ConsultationUpdate>
    fun getOngoingConsultationUpdate(consultationId: String): ConsultationUpdate?
    fun getFinishedConsultationUpdate(consultationId: String): ConsultationUpdate?
}
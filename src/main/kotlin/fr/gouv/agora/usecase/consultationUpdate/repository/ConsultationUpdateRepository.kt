package fr.gouv.agora.usecase.consultationUpdate.repository

import fr.gouv.agora.domain.ConsultationUpdate

@Deprecated("Should use ConsultationUpdateV2Repository instead")
@Suppress("DEPRECATION")
interface ConsultationUpdateRepository {
    fun getConsultationUpdates(consultationIDs: List<String>): List<ConsultationUpdate>
    fun getOngoingConsultationUpdate(consultationId: String): ConsultationUpdate?
    fun getFinishedConsultationUpdate(consultationId: String): ConsultationUpdate?
}
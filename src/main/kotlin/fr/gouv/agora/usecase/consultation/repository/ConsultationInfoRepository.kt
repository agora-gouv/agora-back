package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing

interface ConsultationInfoRepository {
    fun getOngoingConsultations(): List<ConsultationPreviewOngoing>
    fun getFinishedConsultations(): List<ConsultationPreviewFinished>
    fun getAnsweredConsultations(userId: String): List<ConsultationWithUpdateInfo>
    fun getConsultation(consultationId: String): ConsultationInfo?
    fun getConsultationsToAggregate(): List<ConsultationInfo>
}

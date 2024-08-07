package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreview

interface ConsultationInfoRepository {
    fun getOngoingConsultations(): List<ConsultationPreview>
    fun getFinishedConsultations(): List<ConsultationPreviewFinished>
    fun getAnsweredConsultations(userId: String): List<ConsultationPreviewFinished>
    fun getConsultation(consultationId: String): ConsultationInfo?
    fun getConsultationsToAggregate(): List<ConsultationPreview>
    fun isConsultationExists(consultationId: String): Boolean
    fun getConsultationByIdOrSlug(consultationIdOrSlug: String): ConsultationInfo?
}

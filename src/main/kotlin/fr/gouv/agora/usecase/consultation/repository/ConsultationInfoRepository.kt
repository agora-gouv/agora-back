package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.Territoire

interface ConsultationInfoRepository {
    fun getOngoingConsultations(userTerritoires: List<Territoire>): List<ConsultationPreview>
    fun getOngoingConsultationsWithUnpublished(userTerritoires: List<Territoire>): List<ConsultationPreview>
    fun getFinishedConsultations(userTerritoires: List<Territoire>): List<ConsultationPreviewFinished>
    fun getFinishedConsultationsWithUnpublished(userTerritoires: List<Territoire>): List<ConsultationPreviewFinished>
    fun getAnsweredConsultations(userId: String): List<ConsultationPreviewFinished>
    fun getConsultation(consultationId: String): ConsultationInfo?
    fun getConsultationsToAggregate(): List<ConsultationPreview>
    fun isConsultationExists(consultationId: String): Boolean
    fun getConsultationByIdOrSlug(consultationIdOrSlug: String): ConsultationInfo?
    fun getConsultationByIdOrSlugWithUnpublished(consultationIdOrSlug: String): ConsultationInfo?
}

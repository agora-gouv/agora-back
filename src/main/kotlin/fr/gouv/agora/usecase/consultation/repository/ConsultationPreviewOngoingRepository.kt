package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewOngoingInfo

interface ConsultationPreviewOngoingRepository {
    fun getConsultationPreviewOngoingList(): List<ConsultationPreviewOngoingInfo>
    fun clearCache()
}
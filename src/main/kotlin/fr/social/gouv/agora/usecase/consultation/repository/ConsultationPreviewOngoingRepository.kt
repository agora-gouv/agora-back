package fr.social.gouv.agora.usecase.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo

interface ConsultationPreviewOngoingRepository {
    fun getConsultationPreviewOngoingList(): List<ConsultationPreviewOngoingInfo>
}
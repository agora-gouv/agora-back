package fr.gouv.agora.usecase.consultationUpdate.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2

interface ConsultationUpdateV2Repository {
    fun getLastConsultationUpdateLabel(consultationId: String): String?
    fun getLastConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2?
    fun getConsultationUpdate(consultationId: String, consultationUpdateId: String): ConsultationUpdateInfoV2?
}
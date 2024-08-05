package fr.gouv.agora.usecase.consultationUpdate.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2

interface ConsultationUpdateV2Repository {
    fun getLatestConsultationUpdateLabel(consultationId: String): String?
    fun getUnansweredUsersConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2?
    fun getLatestConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2?
    fun getConsultationUpdate(consultationId: String, consultationUpdateId: String): ConsultationUpdateInfoV2?
    fun getConsultationUpdateId(consultationId: String, slug: String): String?
}

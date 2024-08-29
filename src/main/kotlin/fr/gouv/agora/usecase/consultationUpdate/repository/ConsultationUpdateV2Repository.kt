package fr.gouv.agora.usecase.consultationUpdate.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2

interface ConsultationUpdateV2Repository {
    fun getUnansweredUsersConsultationUpdateWithUnpublished(consultationId: String): ConsultationUpdateInfoV2?
    fun getLatestConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2?
    fun getConsultationUpdate(consultationId: String, consultationUpdateId: String): ConsultationUpdateInfoV2?
    fun getConsultationUpdateBySlugOrId(
        consultationId: String,
        consultationUpdateIdOrSlug: String
    ): ConsultationUpdateInfoV2?
    fun getConsultationUpdateBySlugOrIdWithUnpublished(
        consultationId: String,
        consultationUpdateIdOrSlug: String
    ): ConsultationUpdateInfoV2?
}

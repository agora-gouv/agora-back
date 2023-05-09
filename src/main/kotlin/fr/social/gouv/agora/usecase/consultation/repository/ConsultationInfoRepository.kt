package fr.social.gouv.agora.usecase.consultation.repository

interface ConsultationInfoRepository {
    fun getConsultation(consultationId: String): ConsultationInfo?
}

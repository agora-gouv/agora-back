package fr.gouv.agora.usecase.consultation.repository

interface ConsultationInfoRepository {
    fun getConsultations(): List<ConsultationInfo>
    fun getConsultationsToAggregate(): List<ConsultationInfo>
    fun getConsultation(consultationId: String): ConsultationInfo?
}

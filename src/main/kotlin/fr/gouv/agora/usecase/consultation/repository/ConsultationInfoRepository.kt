package fr.gouv.agora.usecase.consultation.repository

interface ConsultationInfoRepository {
    @Deprecated("Should not be used anymore")
    fun getConsultations(): List<ConsultationInfo>
    fun getOngoingConsultations(): List<ConsultationInfo>
    fun getFinishedConsultations(): List<ConsultationWithUpdateInfo>
    fun getAnsweredConsultations(userId: String): List<ConsultationWithUpdateInfo>
    fun getConsultation(consultationId: String): ConsultationInfo?
    fun getConsultationsToAggregate(): List<ConsultationInfo>
}

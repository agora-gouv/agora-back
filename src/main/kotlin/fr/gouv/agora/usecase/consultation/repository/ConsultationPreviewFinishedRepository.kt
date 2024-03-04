package fr.gouv.agora.usecase.consultation.repository

interface ConsultationPreviewFinishedRepository {
    fun getConsultationFinishedCount(): Int
    fun getConsultationFinishedList(offset: Int): List<ConsultationWithUpdateInfo>
}
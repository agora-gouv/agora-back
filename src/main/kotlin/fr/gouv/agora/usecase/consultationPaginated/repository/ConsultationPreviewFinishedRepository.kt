package fr.gouv.agora.usecase.consultationPaginated.repository

import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo

interface ConsultationPreviewFinishedRepository {
    fun getConsultationFinishedCount(): Int
    fun getConsultationFinishedList(offset: Int): List<ConsultationWithUpdateInfo>
}
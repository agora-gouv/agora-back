package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinishedInfo

interface ConsultationPreviewFinishedRepository {
    fun getConsultationFinishedList(): List<ConsultationPreviewFinishedInfo>
    fun clearCache()
}
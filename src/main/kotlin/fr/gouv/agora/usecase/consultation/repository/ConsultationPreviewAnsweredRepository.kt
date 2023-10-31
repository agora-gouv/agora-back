package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewAnsweredInfo

interface ConsultationPreviewAnsweredRepository {
    fun getConsultationAnsweredList(userId: String): List<ConsultationPreviewAnsweredInfo>
    fun deleteConsultationAnsweredListFromCache(userId: String)
}
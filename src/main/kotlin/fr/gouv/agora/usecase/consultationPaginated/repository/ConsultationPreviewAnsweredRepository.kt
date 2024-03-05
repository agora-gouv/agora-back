package fr.gouv.agora.usecase.consultationPaginated.repository

import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo

interface ConsultationPreviewAnsweredRepository {
    fun getConsultationAnsweredCount(userId: String): Int
    fun getConsultationAnsweredList(userId: String, offset: Int): List<ConsultationWithUpdateInfo>
}
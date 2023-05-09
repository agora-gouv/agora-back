package fr.social.gouv.agora.usecase.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewAnsweredInfo

interface ConsultationPreviewAnsweredRepository {
    fun getConsultationAnsweredList(userId: String): List<ConsultationPreviewAnsweredInfo>?
}
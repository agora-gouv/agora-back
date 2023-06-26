package fr.social.gouv.agora.usecase.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewFinishedInfo

interface ConsultationPreviewFinishedRepository {
    fun getConsultationFinishedList(): List<ConsultationPreviewFinishedInfo>
}
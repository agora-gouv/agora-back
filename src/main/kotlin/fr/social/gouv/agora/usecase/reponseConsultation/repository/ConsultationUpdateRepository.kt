package fr.social.gouv.agora.usecase.reponseConsultation.repository

import fr.social.gouv.agora.domain.ConsultationUpdate

interface ConsultationUpdateRepository {
    fun getConsultationUpdate(consultationId: String): ConsultationUpdate?
}
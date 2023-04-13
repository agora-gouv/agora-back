package fr.social.gouv.agora.usecase.consultation.repository

import fr.social.gouv.agora.domain.Consultation

interface ConsultationRepository {
    fun getConsultation(consultationId: String): Consultation?
}
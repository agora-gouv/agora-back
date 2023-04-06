package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.Consultation

interface ConsultationRepository {
    fun getConsultation(id: String): Consultation
}
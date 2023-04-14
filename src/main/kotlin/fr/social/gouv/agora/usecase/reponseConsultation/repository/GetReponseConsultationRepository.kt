package fr.social.gouv.agora.usecase.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation

interface GetReponseConsultationRepository {
    fun getConsultationResponses(consultationId: String): List<ReponseConsultation>
}
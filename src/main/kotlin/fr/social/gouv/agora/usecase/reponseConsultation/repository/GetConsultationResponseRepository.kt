package fr.social.gouv.agora.usecase.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation

interface GetConsultationResponseRepository {
    fun getConsultationResponses(consultationId: String): List<ReponseConsultation>
}
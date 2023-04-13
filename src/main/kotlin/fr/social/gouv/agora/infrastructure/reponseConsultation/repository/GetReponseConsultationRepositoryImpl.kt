package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.usecase.reponseConsultation.GetReponseConsultationRepository

class GetReponseConsultationRepositoryImpl: GetReponseConsultationRepository {

    override fun getConsultationResponses(consultationId: String): List<ReponseConsultation> {
        TODO("Not yet implemented")
    }

}
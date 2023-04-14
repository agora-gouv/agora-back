package fr.social.gouv.agora.usecase.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.InsertStatus

interface ReponseConsultationRepository {
    fun insertReponseConsultation(reponseConsultation: ReponseConsultation): InsertStatus
}

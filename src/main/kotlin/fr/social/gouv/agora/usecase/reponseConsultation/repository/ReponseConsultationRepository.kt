package fr.social.gouv.agora.usecase.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.InsertStatus

interface ReponseConsultationRepository {
    fun insertReponseConsultation(reponseConsultation: ReponseConsultationInserting): InsertStatus
}

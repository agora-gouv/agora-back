package fr.social.gouv.agora.usecase.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation

interface GetConsultationResponseRepository {
    fun getConsultationResponses(consultationId: String): List<ReponseConsultation>
    fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean
    fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean>
}
package fr.gouv.agora.usecase.reponseConsultation.repository

import fr.gouv.agora.domain.ReponseConsultation

interface GetConsultationResponseRepository {
    fun getConsultationResponses(consultationId: String): List<ReponseConsultation>
    fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean
    fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean>
    fun getUsersAnsweredConsultation(consultationId: String): List<String>
}
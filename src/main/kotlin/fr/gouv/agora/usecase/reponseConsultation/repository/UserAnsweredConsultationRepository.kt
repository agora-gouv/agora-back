package fr.gouv.agora.usecase.reponseConsultation.repository

interface UserAnsweredConsultationRepository {
    fun getParticipantCount(consultationId: String): Int
    fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean
    fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean>
    fun getUsersAnsweredConsultation(consultationId: String): List<String>
}
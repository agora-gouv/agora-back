package fr.gouv.agora.usecase.reponseConsultation.repository

import fr.gouv.agora.domain.UserAnsweredConsultation

interface UserAnsweredConsultationRepository {
    fun getParticipantCount(consultationId: String): Int
    fun getAnsweredConsultationIds(userId: String): List<String>
    fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean
    fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean>
    fun getUsersAnsweredConsultation(consultationId: String): List<String>
    fun insertUserAnsweredConsultation(userAnsweredConsultation: UserAnsweredConsultation): UserAnsweredConsultationResult
}

enum class UserAnsweredConsultationResult {
    SUCCESS, FAILURE
}
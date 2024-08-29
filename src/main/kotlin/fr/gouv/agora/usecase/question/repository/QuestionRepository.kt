package fr.gouv.agora.usecase.question.repository

import fr.gouv.agora.domain.Questions

interface QuestionRepository {
    fun getConsultationQuestions(consultationId: String): Questions
}

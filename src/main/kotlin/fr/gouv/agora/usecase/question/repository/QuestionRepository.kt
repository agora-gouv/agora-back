package fr.gouv.agora.usecase.question.repository

import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.Questions

interface QuestionRepository {
    fun getConsultationQuestions(consultationId: String): Questions
    fun getConsultationQuestionList(consultationId: String): List<Question>
}

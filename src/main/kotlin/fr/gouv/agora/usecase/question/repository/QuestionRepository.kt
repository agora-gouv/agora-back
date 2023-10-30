package fr.gouv.agora.usecase.question.repository

import fr.gouv.agora.domain.Question

interface QuestionRepository {
    fun getConsultationQuestionList(consultationId : String): List<Question>
}

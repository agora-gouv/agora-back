package fr.social.gouv.agora.usecase.question.repository

import fr.social.gouv.agora.domain.Question

interface QuestionRepository {
    fun getConsultationQuestionList(idConsultation : String): List<Question>
}

package fr.gouv.agora.usecase.question

import fr.gouv.agora.domain.Questions
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ListQuestionConsultationUseCase(private val repository: QuestionRepository) {

    fun getConsultationQuestions(consultationId: String): Questions {
        return repository.getConsultationQuestions(consultationId)
    }

}

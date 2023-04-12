package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.usecase.question.ListQuestionConsultationUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")

class QuestionController(
    private val listQuestionConsultationUseCase: ListQuestionConsultationUseCase,
    private val jsonMapper: QuestionJsonMapper
) {
    @GetMapping("/consultations/{id}/questions")
    fun getQuestions(@PathVariable id: String): HttpEntity<*> {
        return listQuestionConsultationUseCase.getQuestionConsultation(id)?.let { listQuestions ->
            ResponseEntity.ok()
                .body(jsonMapper.toJson(listQuestions))
        } ?: ResponseEntity.EMPTY
    }
}
package fr.gouv.agora.infrastructure.question

import fr.gouv.agora.usecase.question.ListQuestionConsultationUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Consultations")
class QuestionController(
    private val listQuestionConsultationUseCase: ListQuestionConsultationUseCase,
    private val jsonMapper: QuestionJsonMapper,
) {
    @Operation(summary = "Get Consultations Questions")
    @GetMapping("/consultations/{consultationId}/questions")
    fun getQuestions(@PathVariable consultationId: String): ResponseEntity<QuestionsJson> {
        val questionsJson = jsonMapper.toJson(listQuestionConsultationUseCase.getConsultationQuestions(consultationId))

        return ResponseEntity.ok().body(questionsJson)
    }

}

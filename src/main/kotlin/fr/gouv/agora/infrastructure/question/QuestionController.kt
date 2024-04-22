package fr.gouv.agora.infrastructure.question

import fr.gouv.agora.infrastructure.question.repository.ConsultationQuestionJsonCacheRepository
import fr.gouv.agora.usecase.question.ListQuestionConsultationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QuestionController(
    private val listQuestionConsultationUseCase: ListQuestionConsultationUseCase,
    private val jsonMapper: QuestionJsonMapper,
    private val cacheRepository: ConsultationQuestionJsonCacheRepository,
) {
    @GetMapping("/consultations/{consultationId}/questions")
    fun getQuestions(@PathVariable consultationId: String): ResponseEntity<QuestionsJson> {
        val questionsJson = cacheRepository.getConsultationQuestions(consultationId)
            ?: getQuestionsFromUseCase(consultationId)

        return ResponseEntity.ok().body(questionsJson)
    }

    private fun getQuestionsFromUseCase(consultationId: String): QuestionsJson {
        return jsonMapper.toJson(listQuestionConsultationUseCase.getConsultationQuestions(consultationId))
            .also { cacheRepository.insertConsultationQuestions(consultationId, it) }
    }
}
package fr.gouv.agora.infrastructure.content

import fr.gouv.agora.usecase.content.GetInformationsPoserMaQuestionUseCase
import fr.gouv.agora.usecase.content.GetInformationsQuestionsUseCase
import fr.gouv.agora.usecase.content.GetInformationsReponsesAVenirUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/content")
@Tag(name = "Content")
class ContentController(
    private val getInformationsQuestions: GetInformationsQuestionsUseCase,
    private val getInformationsReponsesAVenir: GetInformationsReponsesAVenirUseCase,
    private val getInformationsPoserMaQuestion: GetInformationsPoserMaQuestionUseCase,
) {
    @Operation(summary = "Récupérer les informations des questions (bottomsheet)")
    @GetMapping("/informations-questions")
    fun getInformationsQuestions(): ResponseEntity<ContentJson> {
        val content = getInformationsQuestions.execute()
        return ResponseEntity.ok().body(ContentJson(content))
    }

    @Operation(summary = "Récupérer les informations des réponses à venir (bottomsheet)")
    @GetMapping("/informations-reponses-a-venir")
    fun getInformationsReponsesAVenir(): ResponseEntity<ContentJson> {
        val content = getInformationsReponsesAVenir.execute()
        return ResponseEntity.ok().body(ContentJson(content))
    }

    @Operation(summary = "Récupérer les informations pour poser une question")
    @GetMapping("/poser-ma-question")
    fun getInformationsPoserMaQuestion(): ResponseEntity<ContentJson> {
        val content = getInformationsPoserMaQuestion.execute()
        return ResponseEntity.ok().body(ContentJson(content))
    }
}

data class ContentJson(val description: String)

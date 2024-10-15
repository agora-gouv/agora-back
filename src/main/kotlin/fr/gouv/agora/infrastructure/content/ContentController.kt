package fr.gouv.agora.infrastructure.content

import fr.gouv.agora.usecase.content.GetContentPagePoserMaQuestionUseCase
import fr.gouv.agora.usecase.content.GetContentQuestionsAuGouvernementUseCase
import fr.gouv.agora.usecase.content.GetContentPageReponseAuxQuestionsAuGouvernementUseCase
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
    private val getContentPageQuestionsAuGouvernementUseCase: GetContentQuestionsAuGouvernementUseCase,
    private val getContentPageReponseAuxQuestionsAuGouvernementUseCase: GetContentPageReponseAuxQuestionsAuGouvernementUseCase,
    private val getContentPagePoserMaQuestionUseCase: GetContentPagePoserMaQuestionUseCase,
) {
    @Operation(summary = "Récupérer les informations de la page Questions au Gouvernement")
    @GetMapping("/page-questions-au-gouvernement")
    fun getContentQuestionsAuGouvernementPage(): ResponseEntity<QuestionsAuGouvernementContentJson> {
        val content = getContentPageQuestionsAuGouvernementUseCase.execute()
        return ResponseEntity.ok().body(QuestionsAuGouvernementContentJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Réponse aux QaGs")
    @GetMapping("/page-reponses-aux-qags")
    fun getContentReponsesAVenirPage(): ResponseEntity<ReponseAuxQagsJson> {
        val content = getContentPageReponseAuxQuestionsAuGouvernementUseCase.execute()
        return ResponseEntity.ok().body(ReponseAuxQagsJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Poser ma Question")
    @GetMapping("/page-poser-ma-question")
    fun getContentPoserMaQuestionPage(): ResponseEntity<PoserMaQuestionJson> {
        val content = getContentPagePoserMaQuestionUseCase.execute()
        return ResponseEntity.ok().body(PoserMaQuestionJson(content))
    }
}

data class QuestionsAuGouvernementContentJson(val info: String)
data class ReponseAuxQagsJson(val infoReponsesAVenir: String)
data class PoserMaQuestionJson(val regles: String)

package fr.gouv.agora.infrastructure.participationCharter

import fr.gouv.agora.usecase.participationCharter.ParticipationCharterUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Charte Participation")
class ParticipationCharterController(
    private val useCase: ParticipationCharterUseCase,
) {
    @GetMapping("/participation_charter")
    fun getParticipationCharterText(): ResponseEntity<ParticipationCharterJson> {
        val participationCharter = useCase.getParticipationCharterText()
        return ResponseEntity.ok()
            .body(
                ParticipationCharterJson(
                    "<body>${participationCharter.text}</body>",
                    "<body>${participationCharter.preview}</body>"
                )
            )
    }
}

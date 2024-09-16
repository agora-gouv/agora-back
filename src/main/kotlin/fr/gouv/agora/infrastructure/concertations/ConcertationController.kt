package fr.gouv.agora.infrastructure.concertations

import fr.gouv.agora.usecase.concertations.ConcertationJsonMapper
import fr.gouv.agora.usecase.concertations.GetConcertationsUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/concertations")
@Tag(name = "Concertations")
class ConcertationController(
    val getConcertationsUseCase: GetConcertationsUseCase,
    val concertationJsonMapper: ConcertationJsonMapper,
) {
    @GetMapping
    fun getConcertations(): ResponseEntity<List<ConcertationJson>> {
        val concertations = getConcertationsUseCase.execute()
        val concertationsJson = concertationJsonMapper.toConcertationJson(concertations)

        return ResponseEntity.ok().body(concertationsJson)
    }
}

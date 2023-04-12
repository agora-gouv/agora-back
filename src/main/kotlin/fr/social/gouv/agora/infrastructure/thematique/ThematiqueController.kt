package fr.social.gouv.agora.infrastructure.thematique

import fr.social.gouv.agora.usecase.thematique.ListThematiqueUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ThematiqueController(
    private val listThematiqueUseCase: ListThematiqueUseCase,
    private val jsonMapper: ThematiqueJsonMapper,
) {
    @GetMapping("/thematiques")
    fun getThematiqueList(): ResponseEntity<ThematiquesJson> {
        return ResponseEntity.ok()
            .body(jsonMapper.toJson(listThematiqueUseCase.getThematiqueList()))
    }
}
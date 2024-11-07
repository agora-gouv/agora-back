package fr.gouv.agora.infrastructure.thematique

import fr.gouv.agora.usecase.thematique.ListThematiqueUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@Tag(name = "Thématiques")
class ThematiqueController(
    private val listThematiqueUseCase: ListThematiqueUseCase,
    private val jsonMapper: ThematiqueJsonMapper,
) {
    @GetMapping("/thematiques")
    fun getThematiqueList(): ResponseEntity<ThematiquesJson> {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
            .body(jsonMapper.toJson(listThematiqueUseCase.getThematiqueList()))
    }
}

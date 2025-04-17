package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.usecase.ficheInventaire.GetFichesInventaireUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "FicheInventaire")
class FicheInventaireController(
    private val getFichesInventaireUseCase: GetFichesInventaireUseCase,
    private val ficheInventaireJsonMapper: FicheInventaireJsonMapper,
) {
    @Operation(summary = "Get all Fiches Inventaire")
    @PostMapping("/fiches-inventaire")
    fun getFichesInventaire(): ResponseEntity<List<FicheInventaireJson>> {
        val ficheInventaires = getFichesInventaireUseCase.execute()
        val fichesInventaireJson = ficheInventaireJsonMapper.toFicheInventaireJson(ficheInventaires)

        return ResponseEntity.ok(fichesInventaireJson)
    }
}

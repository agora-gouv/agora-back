package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.usecase.ficheInventaire.GetFicheInventaireUseCase
import fr.gouv.agora.usecase.ficheInventaire.GetFichesInventaireUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "FicheInventaire")
class FicheInventaireController(
    private val getFichesInventaireUseCase: GetFichesInventaireUseCase,
    private val getFicheInventaireUseCase: GetFicheInventaireUseCase,
    private val ficheInventaireJsonMapper: FicheInventaireJsonMapper,
) {
    @Operation(summary = "Get all Fiches Inventaire")
    @GetMapping("/fiches_inventaire")
    fun getFichesInventaire(): ResponseEntity<List<FicheInventaireJson>> {
        val fichesInventaire = getFichesInventaireUseCase.execute()
        val fichesInventaireJson = fichesInventaire.map {
            ficheInventaireJsonMapper.toFicheInventaireJson(it)
        }

        return ResponseEntity.ok(fichesInventaireJson)
    }

    @Operation(summary = "Get Fiches Inventaire")
    @GetMapping("/fiches_inventaire/{idFiche}")
    fun getFichesInventaire(@PathVariable idFiche: String): ResponseEntity<FicheInventaireJson> {
        val ficheInventaire = getFicheInventaireUseCase.execute(idFiche)
        val fichesInventaireJson = ficheInventaireJsonMapper.toFicheInventaireJson(ficheInventaire)

        return ResponseEntity.ok(fichesInventaireJson)
    }
}

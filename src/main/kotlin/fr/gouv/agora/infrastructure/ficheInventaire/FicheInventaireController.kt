package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.infrastructure.notification.dto.NotificationMessageDTO
import fr.gouv.agora.usecase.ficheInventaire.GetFicheInventaireUseCase
import fr.gouv.agora.usecase.ficheInventaire.GetFichesInventaireUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "FicheInventaire")
class FicheInventaireController(
    private val getFichesInventaireUseCase: GetFichesInventaireUseCase,
    private val getFicheInventaireUseCase: GetFicheInventaireUseCase,
    private val ficheInventaireJsonMapper: FicheInventaireJsonMapper,
) {
    private val MODALITES = setOf("En présentiel", "En ligne" )
    private val CONDITIONS = setOf("Ouvert à tous", "Tirage au sort représentatif" )

    @Operation(summary = "Get all Fiches Inventaire")
    @GetMapping("/fiches_inventaire")
    fun getFichesInventaireList(@RequestParam("titre") titre: String?, @RequestParam("thematique") thematique: String?, @RequestParam("etape") etape: List<String>?, @RequestParam("participation") participation: List<String>?, @RequestParam("anneeDeLancement") anneeDeLancement: String?): ResponseEntity<List<FicheInventaireJson>> {

        var titreFiltre = titre.takeUnless { it.isNullOrBlank() }
        var thematiqueFiltre = thematique.takeUnless { it.isNullOrBlank() }
        var etapeFiltre = etape.takeUnless { it?.filterNot { it.isEmpty() }.isNullOrEmpty() }
        var anneeDeLancementFiltre = anneeDeLancement.takeUnless { it.isNullOrBlank() }
        val modaliteFiltre = participation.takeUnless { it?.filterNot { it.isEmpty() }.isNullOrEmpty() }?.filter {MODALITES.contains(it)}
        val conditionFiltre = participation.takeUnless { it?.filterNot { it.isEmpty() }.isNullOrEmpty() }?.filter {CONDITIONS.contains(it)}

        val fichesInventaire = getFichesInventaireUseCase.execute(titreFiltre, thematiqueFiltre, etapeFiltre, conditionFiltre,modaliteFiltre, anneeDeLancementFiltre)
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

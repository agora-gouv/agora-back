package fr.gouv.agora.infrastructure.referentiel

import fr.gouv.agora.domain.Territoire
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/referentiels")
@Tag(name = "Référentiels")
class ReferentielController {
    @Operation(summary = "Récupérer les régions et les départements français")
    @GetMapping("/regions-et-departements")
    fun getRegionsEtDepartements(): ResponseEntity<TerritoiresJson> {
        val regionsEtDepartements = Territoire.Region.values().map {
            RegionJson(it.value, it.getDepartementsNames())
        }

        val territoires = TerritoiresJson(regionsEtDepartements, Territoire.Pays.values().map { it.value })

        return ResponseEntity.ok().body(territoires)
    }
}

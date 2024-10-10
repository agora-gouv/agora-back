package fr.gouv.agora.infrastructure.referentiel

import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.domain.Territoire.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/referentiels")
@Tag(name = "Référentiels")
class ReferentielController(val territoiresJsonMapper: TerritoiresJsonMapper) {
    @Operation(summary = "Récupérer les régions et les départements français")
    @GetMapping("/regions-et-departements")
    fun getRegionsEtDepartements(): ResponseEntity<TerritoiresJson> {
        val territoires = territoiresJsonMapper.toJson(Region.values(), Pays.values())

        return ResponseEntity.ok().body(territoires)
    }
}

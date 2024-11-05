package fr.gouv.agora.infrastructure.referentiel

import fr.gouv.agora.domain.Territoire.Pays
import fr.gouv.agora.domain.Territoire.Region
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/referentiels")
@Tag(name = "Référentiels")
class ReferentielController(val territoiresJsonMapper: TerritoiresJsonMapper) {
    @Operation(summary = "Récupérer les régions et les départements français")
    @GetMapping("/regions-et-departements")
    fun getRegionsEtDepartements(): ResponseEntity<TerritoiresJson> {
        val territoires = territoiresJsonMapper.toJson(Region.values(), Pays.values())

        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
            .body(territoires)
    }
}

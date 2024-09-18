package fr.gouv.agora.infrastructure.referentiel

import fr.gouv.agora.domain.Region
import fr.gouv.agora.domain.Region.AUVERGNE_RHONE_ALPES
import fr.gouv.agora.domain.Region.BOURGOGNE_FRANCHE_COMTE
import fr.gouv.agora.domain.Region.BRETAGNE
import fr.gouv.agora.domain.Region.CENTRE_VAL_DE_LOIRE
import fr.gouv.agora.domain.Region.CORSE
import fr.gouv.agora.domain.Region.GRAND_EST
import fr.gouv.agora.domain.Region.GUADELOUPE
import fr.gouv.agora.domain.Region.GUYANE
import fr.gouv.agora.domain.Region.HAUTS_DE_FRANCE
import fr.gouv.agora.domain.Region.ILE_DE_FRANCE
import fr.gouv.agora.domain.Region.LA_REUNION
import fr.gouv.agora.domain.Region.MARTINIQUE
import fr.gouv.agora.domain.Region.MAYOTTE
import fr.gouv.agora.domain.Region.NORMANDIE
import fr.gouv.agora.domain.Region.NOUVELLE_AQUITAINE
import fr.gouv.agora.domain.Region.NOUVELLE_CALEDONIE
import fr.gouv.agora.domain.Region.OCCITANIE
import fr.gouv.agora.domain.Region.PAYS_DE_LA_LOIRE
import fr.gouv.agora.domain.Region.POLYNESIE_FRANCAISE
import fr.gouv.agora.domain.Region.PROVENCE_ALPES_COTE_D_AZUR
import fr.gouv.agora.domain.Region.SAINT_BARTHELEMY
import fr.gouv.agora.domain.Region.SAINT_MARTIN
import fr.gouv.agora.domain.Region.SAINT_PIERRE_ET_MIQUELON
import fr.gouv.agora.domain.Region.TERRES_AUSTRALES_ET_ANTARCTIQUES_FRANCAISES
import fr.gouv.agora.domain.Region.WALLIS_ET_FUTUNA
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
    fun getRegionsEtDepartements(): ResponseEntity<List<RegionJson>> {
        val regionsEtDepartements = Region.values().map {
            RegionJson(it.value, it.getDepartementsNames())
        }

        return ResponseEntity.ok().body(regionsEtDepartements)
    }
}

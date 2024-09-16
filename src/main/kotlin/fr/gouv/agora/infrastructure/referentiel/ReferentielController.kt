package fr.gouv.agora.infrastructure.referentiel

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
        val regionsEtDepartements = listOf(
            RegionJson(
                "Auvergne-Rhône-Alpes", listOf(
                    "Ain",
                    "Allier",
                    "Ardèche",
                    "Cantal",
                    "Drôme",
                    "Isère",
                    "Loire",
                    "Haute-Loire",
                    "Puy-de-Dôme",
                    "Rhône",
                    "Savoie",
                    "Haute-Savoie",
                )
            ),
            RegionJson(
                "Bourgogne-Franche-Comté", listOf(
                    "Côte-d’Or",
                    "Doubs",
                    "Jura",
                    "Nièvre",
                    "Haute-Saône",
                    "Saône-et-Loire",
                    "Yonne",
                    "Territoire de Belfort",
                )
            ),
            RegionJson(
                "Bretagne", listOf(
                    "Côtes-d’Armor",
                    "Finistère",
                    "Ille-et-Vilaine",
                    "Morbihan",
                )
            ),
            RegionJson(
                "Centre-Val de Loire", listOf(
                    "Cher",
                    "Eure-et-Loir",
                    "Indre",
                    "Indre-et-Loire",
                    "Loir-et-Cher",
                    "Loiret",
                )
            ),
            RegionJson(
                "Corse", listOf(
                    "Corse-du-Sud",
                    "Haute-Corse",
                )
            ),
            RegionJson(
                "Grand Est", listOf(
                    "Ardennes",
                    "Aube",
                    "Marne",
                    "Haute-Marne",
                    "Meurthe-et-Moselle",
                    "Meuse",
                    "Moselle",
                    "Bas-Rhin",
                    "Haut-Rhin",
                    "Vosges",
                )
            ),
            RegionJson(
                "Hauts-de-France", listOf(
                    "Aisne",
                    "Nord",
                    "Oise",
                    "Pas-de-Calais",
                    "Somme",
                )
            ),
            RegionJson(
                "Ile-de-France", listOf(
                    "Paris",
                    "Seine-et-Marne",
                    "Yvelines",
                    "Essonne",
                    "Hauts-de-Seine",
                    "Seine-Saint-Denis",
                    "Val-de-Marne",
                    "Val-d’Oise",
                )
            ),
            RegionJson(
                "Normandie", listOf(
                    "Calvados",
                    "Eure",
                    "Manche",
                    "Orne",
                    "Seine-Maritime",
                )
            ),
            RegionJson(
                "Nouvelle-Aquitaine", listOf(
                    "Charente",
                    "Charente-Maritime",
                    "Corrèze",
                    "Creuse",
                    "Dordogne",
                    "Gironde",
                    "Landes",
                    "Lot-et-Garonne",
                    "Pyrénées-Atlantiques",
                    "Deux-Sèvres",
                    "Vienne",
                    "Haute-Vienne",
                )
            ),
            RegionJson(
                "Occitanie", listOf(
                    "Ariège",
                    "Aude",
                    "Aveyron",
                    "Gard",
                    "Haute-Garonne",
                    "Gers",
                    "Hérault",
                    "Lot",
                    "Lozère",
                    "Hautes-Pyrénées",
                    "Pyrénées-Orientales",
                    "Tarn",
                    "Tarn-et-Garonne",
                )
            ),
            RegionJson(
                "Pays de la Loire", listOf(
                    "Loire-Atlantique",
                    "Maine-et-Loire",
                    "Mayenne",
                    "Sarthe",
                    "Vendée",
                )
            ),
            RegionJson(
                "Provence Alpes Côte d’Azur", listOf(
                    "Alpes-de-Haute-Provence",
                    "Hautes-Alpes",
                    "Alpes-Maritimes",
                    "Bouches-du-Rhône",
                    "Var",
                    "Vaucluse",
                )
            ),
            RegionJson("Guadeloupe", emptyList()),
            RegionJson("Martinique", emptyList()),
            RegionJson("Guyane", emptyList()),
            RegionJson("La Réunion", emptyList()),
            RegionJson("Mayotte", emptyList()),
            RegionJson("Saint-Pierre-et-Miquelon", emptyList()),
            RegionJson("Saint-Barthélémy", emptyList()),
            RegionJson("Saint-Martin", emptyList()),
            RegionJson("Terres australes et antarctiques françaises", emptyList()),
            RegionJson("Wallis-et-Futuna", emptyList()),
            RegionJson("Polynésie française", emptyList()),
            RegionJson("Nouvelle-Calédonie", emptyList()),
        )

        return ResponseEntity.ok().body(regionsEtDepartements)
    }
}

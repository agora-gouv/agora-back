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
    fun getRegionsEtDepartements(): ResponseEntity<Map<String, List<String>>> {
        val regionsEtDepartements = mapOf(
            "Auvergne-Rhône-Alpes" to listOf(
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
            ),
            "Bourgogne-Franche-Comté" to listOf(
                "Côte-d’Or",
                "Doubs",
                "Jura",
                "Nièvre",
                "Haute-Saône",
                "Saône-et-Loire",
                "Yonne",
                "Territoire de Belfort",
            ),
            "Bretagne" to listOf(
                "Côtes-d’Armor",
                "Finistère",
                "Ille-et-Vilaine",
                "Morbihan",
            ),
            "Centre-Val de Loire" to listOf(
                "Cher",
                "Eure-et-Loir",
                "Indre",
                "Indre-et-Loire",
                "Loir-et-Cher",
                "Loiret",
            ),
            "Corse" to listOf(
                "Corse-du-Sud",
                "Haute-Corse",
            ),
            "Grand Est" to listOf(
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
            ),
            "Hauts-de-France" to listOf(
                "Aisne",
                "Nord",
                "Oise",
                "Pas-de-Calais",
                "Somme",
            ),
            "Ile-de-France" to listOf(
                "Paris",
                "Seine-et-Marne",
                "Yvelines",
                "Essonne",
                "Hauts-de-Seine",
                "Seine-Saint-Denis",
                "Val-de-Marne",
                "Val-d’Oise",
            ),
            "Normandie" to listOf(
                "Calvados",
                "Eure",
                "Manche",
                "Orne",
                "Seine-Maritime",
            ),
            "Nouvelle-Aquitaine" to listOf(
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
            ),
            "Occitanie" to listOf(
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
            ),
            "Pays de la Loire" to listOf(
                "Loire-Atlantique",
                "Maine-et-Loire",
                "Mayenne",
                "Sarthe",
                "Vendée",
            ),
            "Provence Alpes Côte d’Azur" to listOf(
                "Alpes-de-Haute-Provence",
                "Hautes-Alpes",
                "Alpes-Maritimes",
                "Bouches-du-Rhône",
                "Var",
                "Vaucluse",
            ),
            "Guadeloupe" to emptyList(),
            "Martinique" to emptyList(),
            "Guyane" to emptyList(),
            "La Réunion" to emptyList(),
            "Mayotte" to emptyList(),
            "Saint-Pierre-et-Miquelon" to emptyList(),
            "Saint-Barthélémy" to emptyList(),
            "Saint-Martin" to emptyList(),
            "Terres australes et antarctiques françaises" to emptyList(),
            "Wallis-et-Futuna" to emptyList(),
            "Polynésie française" to emptyList(),
            "Nouvelle-Calédonie" to emptyList(),
        )
        return ResponseEntity.ok().body(regionsEtDepartements)
    }
}

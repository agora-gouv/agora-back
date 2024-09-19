package fr.gouv.agora.domain

import fr.gouv.agora.domain.exceptions.InvalidTerritoryException

interface Territoire {
    val value: String

    companion object {
        fun from(territoire: String): Territoire {
            val territoires = listOf(*Pays.values(), *Region.values(), *Departement.values())

            territoires.firstOrNull { it.name == territoire }
                ?.let { return it }
                ?: throw InvalidTerritoryException(territoire)
        }
    }

    enum class Pays(override val value: String) : Territoire {
        FRANCE("National"),
        HORS_DE_FRANCE("Hors de France");
    }

    enum class Region(override val value: String, private val departements: List<Departement>) : Territoire {
        AUVERGNE_RHONE_ALPES(
            "Auvergne-Rhône-Alpes", listOf(
                Departement.AIN,
                Departement.ALLIER,
                Departement.ARDECHE,
                Departement.CANTAL,
                Departement.DROME,
                Departement.ISERE,
                Departement.LOIRE,
                Departement.HAUTE_LOIRE,
                Departement.PUY_DE_DOME,
                Departement.RHONE,
                Departement.SAVOIE,
                Departement.HAUTE_SAVOIE,
            )
        ),
        BOURGOGNE_FRANCHE_COMTE(
            "Bourgogne-Franche-Comté", listOf(
                Departement.COTE_D_OR,
                Departement.DOUBS,
                Departement.JURA,
                Departement.NIEVRE,
                Departement.HAUTE_SAONE,
                Departement.SAONE_ET_LOIRE,
                Departement.YONNE,
                Departement.TERRITOIRE_DE_BELFORT,
            )
        ),
        BRETAGNE(
            "Bretagne", listOf(
                Departement.COTES_D_ARMOR,
                Departement.FINISTERE,
                Departement.ILLE_ET_VILAINE,
                Departement.MORBIHAN,
            )
        ),
        CENTRE_VAL_DE_LOIRE(
            "Centre-Val de Loire", listOf(
                Departement.CHER,
                Departement.EURE_ET_LOIR,
                Departement.INDRE,
                Departement.INDRE_ET_LOIRE,
                Departement.LOIR_ET_CHER,
                Departement.LOIRET,
            )
        ),
        CORSE(
            "Corse", listOf(
                Departement.CORSE_DU_SUD,
                Departement.HAUTE_CORSE,
            )
        ),
        GRAND_EST(
            "Grand Est", listOf(
                Departement.ARDENNES,
                Departement.AUBE,
                Departement.MARNE,
                Departement.HAUTE_MARNE,
                Departement.MEURTHE_ET_MOSELLE,
                Departement.MEUSE,
                Departement.MOSELLE,
                Departement.BAS_RHIN,
                Departement.HAUT_RHIN,
                Departement.VOSGES,
            )
        ),
        HAUTS_DE_FRANCE(
            "Hauts-de-France", listOf(
                Departement.AISNE,
                Departement.NORD,
                Departement.OISE,
                Departement.PAS_DE_CALAIS,
                Departement.SOMME,
            )
        ),
        ILE_DE_FRANCE(
            "Ile-de-France", listOf(
                Departement.PARIS,
                Departement.SEINE_ET_MARNE,
                Departement.YVELINES,
                Departement.ESSONNE,
                Departement.HAUTS_DE_SEINE,
                Departement.SEINE_SAINT_DENIS,
                Departement.VAL_DE_MARNE,
                Departement.VAL_D_OISE,
            )
        ),
        NORMANDIE(
            "Normandie", listOf(
                Departement.CALVADOS,
                Departement.EURE,
                Departement.MANCHE,
                Departement.ORNE,
                Departement.SEINE_MARITIME,
            )
        ),
        NOUVELLE_AQUITAINE(
            "Nouvelle-Aquitaine", listOf(
                Departement.CHARENTE,
                Departement.CHARENTE_MARITIME,
                Departement.CORREZE,
                Departement.CREUSE,
                Departement.DORDOGNE,
                Departement.GIRONDE,
                Departement.LANDES,
                Departement.LOT_ET_GARONNE,
                Departement.PYRENEES_ATLANTIQUES,
                Departement.DEUX_SEVRES,
                Departement.VIENNE,
                Departement.HAUTE_VIENNE,
            )
        ),
        OCCITANIE(
            "Occitanie", listOf(
                Departement.ARIEGE,
                Departement.AUDE,
                Departement.AVEYRON,
                Departement.GARD,
                Departement.HAUTE_GARONNE,
                Departement.GERS,
                Departement.HERAULT,
                Departement.LOT,
                Departement.LOZERE,
                Departement.HAUTES_PYRENEES,
                Departement.PYRENEES_ORIENTALES,
                Departement.TARN,
                Departement.TARN_ET_GARONNE,
            )
        ),
        PAYS_DE_LA_LOIRE(
            "Pays de la Loire", listOf(
                Departement.LOIRE_ATLANTIQUE,
                Departement.MAINE_ET_LOIRE,
                Departement.MAYENNE,
                Departement.SARTHE,
                Departement.VENDEE,
            )
        ),
        PROVENCE_ALPES_COTE_D_AZUR(
            "Provence Alpes Côte d’Azur", listOf(
                Departement.ALPES_DE_HAUTE_PROVENCE,
                Departement.HAUTES_ALPES,
                Departement.ALPES_MARITIMES,
                Departement.BOUCHES_DU_RHONE,
                Departement.VAR,
                Departement.VAUCLUSE,
            )
        ),
        GUADELOUPE("Guadeloupe", emptyList()),
        MARTINIQUE("Martinique", emptyList()),
        GUYANE("Guyane", emptyList()),
        LA_REUNION("La Réunion", emptyList()),
        MAYOTTE("Mayotte", emptyList()),
        SAINT_PIERRE_ET_MIQUELON("Saint-Pierre-et-Miquelon", emptyList()),
        SAINT_BARTHELEMY("Saint-Barthélémy", emptyList()),
        SAINT_MARTIN("Saint-Martin", emptyList()),
        TERRES_AUSTRALES_ET_ANTARCTIQUES_FRANCAISES("Terres australes et antarctiques françaises", emptyList()),
        WALLIS_ET_FUTUNA("Wallis-et-Futuna", emptyList()),
        POLYNESIE_FRANCAISE("Polynésie française", emptyList()),
        NOUVELLE_CALEDONIE("Nouvelle-Calédonie", emptyList());

        fun getDepartementsNames(): List<String> {
            return this.departements.map { it.value }
        }
    }

    enum class Departement(override val value: String) : Territoire {
        AIN("Ain"),
        ALLIER("Allier"),
        ARDECHE("Ardèche"),
        CANTAL("Cantal"),
        DROME("Drôme"),
        ISERE("Isère"),
        LOIRE("Loire"),
        HAUTE_LOIRE("Haute-Loire"),
        PUY_DE_DOME("Puy-de-Dôme"),
        RHONE("Rhône"),
        SAVOIE("Savoie"),
        HAUTE_SAVOIE("Haute-Savoie"),
        COTE_D_OR("Côte-d’Or"),
        DOUBS("Doubs"),
        JURA("Jura"),
        NIEVRE("Nièvre"),
        HAUTE_SAONE("Haute-Saône"),
        SAONE_ET_LOIRE("Saône-et-Loire"),
        YONNE("Yonne"),
        TERRITOIRE_DE_BELFORT("Territoire de Belfort"),
        COTES_D_ARMOR("Côtes-d’Armor"),
        FINISTERE("Finistère"),
        ILLE_ET_VILAINE("Ille-et-Vilaine"),
        MORBIHAN("Morbihan"),
        CHER("Cher"),
        EURE_ET_LOIR("Eure-et-Loir"),
        INDRE("Indre"),
        INDRE_ET_LOIRE("Indre-et-Loire"),
        LOIR_ET_CHER("Loir-et-Cher"),
        LOIRET("Loiret"),
        CORSE_DU_SUD("Corse-du-Sud"),
        HAUTE_CORSE("Haute-Corse"),
        ARDENNES("Ardennes"),
        AUBE("Aube"),
        MARNE("Marne"),
        HAUTE_MARNE("Haute-Marne"),
        MEURTHE_ET_MOSELLE("Meurthe-et-Moselle"),
        MEUSE("Meuse"),
        MOSELLE("Moselle"),
        BAS_RHIN("Bas-Rhin"),
        HAUT_RHIN("Haut-Rhin"),
        VOSGES("Vosges"),
        AISNE("Aisne"),
        NORD("Nord"),
        OISE("Oise"),
        PAS_DE_CALAIS("Pas-de-Calais"),
        SOMME("Somme"),
        PARIS("Paris"),
        SEINE_ET_MARNE("Seine-et-Marne"),
        YVELINES("Yvelines"),
        ESSONNE("Essonne"),
        HAUTS_DE_SEINE("Hauts-de-Seine"),
        SEINE_SAINT_DENIS("Seine-Saint-Denis"),
        VAL_DE_MARNE("Val-de-Marne"),
        VAL_D_OISE("Val-d’Oise"),
        CALVADOS("Calvados"),
        EURE("Eure"),
        MANCHE("Manche"),
        ORNE("Orne"),
        SEINE_MARITIME("Seine-Maritime"),
        CHARENTE("Charente"),
        CHARENTE_MARITIME("Charente-Maritime"),
        CORREZE("Corrèze"),
        CREUSE("Creuse"),
        DORDOGNE("Dordogne"),
        GIRONDE("Gironde"),
        LANDES("Landes"),
        LOT_ET_GARONNE("Lot-et-Garonne"),
        PYRENEES_ATLANTIQUES("Pyrénées-Atlantiques"),
        DEUX_SEVRES("Deux-Sèvres"),
        VIENNE("Vienne"),
        HAUTE_VIENNE("Haute-Vienne"),
        ARIEGE("Ariège"),
        AUDE("Aude"),
        AVEYRON("Aveyron"),
        GARD("Gard"),
        HAUTE_GARONNE("Haute-Garonne"),
        GERS("Gers"),
        HERAULT("Hérault"),
        LOT("Lot"),
        LOZERE("Lozère"),
        HAUTES_PYRENEES("Hautes-Pyrénées"),
        PYRENEES_ORIENTALES("Pyrénées-Orientales"),
        TARN("Tarn"),
        TARN_ET_GARONNE("Tarn-et-Garonne"),
        LOIRE_ATLANTIQUE("Loire-Atlantique"),
        MAINE_ET_LOIRE("Maine-et-Loire"),
        MAYENNE("Mayenne"),
        SARTHE("Sarthe"),
        VENDEE("Vendée"),
        ALPES_DE_HAUTE_PROVENCE("Alpes-de-Haute-Provence"),
        HAUTES_ALPES("Hautes-Alpes"),
        ALPES_MARITIMES("Alpes-Maritimes"),
        BOUCHES_DU_RHONE("Bouches-du-Rhône"),
        VAR("Var"),
        VAUCLUSE("Vaucluse");
    }
}

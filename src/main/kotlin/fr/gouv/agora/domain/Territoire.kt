package fr.gouv.agora.domain

import fr.gouv.agora.domain.exceptions.InvalidTerritoryException

interface Territoire {
    val value: String

    companion object {
        fun from(territoire: String): Territoire {
            val territoires = listOf(*Pays.values(), *Region.values(), *Departement.values())
            territoires.firstOrNull { it.value.lowercase() == territoire.lowercase() }
                ?.let { return it }
                ?: throw InvalidTerritoryException(territoire)
        }

        fun of(userProfile: Profile?): List<Territoire> {
            val primaryDepartment = userProfile?.primaryDepartment
            val secondaryDepartment = userProfile?.secondaryDepartment
            val primaryRegion = Region.getByDepartment(primaryDepartment)
            val secondaryRegion = Region.getByDepartment(secondaryDepartment)

            return listOfNotNull(
                Pays.FRANCE, primaryDepartment, secondaryDepartment,
                primaryRegion, secondaryRegion
            ).distinct()
        }
    }

    enum class Pays(override val value: String) : Territoire {
        FRANCE("National"),
        HORS_DE_FRANCE("Français de l'étranger");
    }

    enum class Region(override val value: String, val departements: List<Departement>) : Territoire {
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
        OUTRE_MER(
            "Outre-mer", listOf(
                Departement.GUADELOUPE,
                Departement.MARTINIQUE,
                Departement.GUYANE,
                Departement.LA_REUNION,
                Departement.MAYOTTE,
                Departement.SAINT_PIERRE_ET_MIQUELON,
                Departement.SAINT_BARTHELEMY,
                Departement.SAINT_MARTIN,
                Departement.TERRES_AUSTRALES_ET_ANTARCTIQUES_FRANCAISES,
                Departement.WALLIS_ET_FUTUNA,
                Departement.POLYNESIE_FRANCAISE,
                Departement.NOUVELLE_CALEDONIE
            )
        );

        fun getDepartementsNames(): List<String> {
            return this.departements.map { it.value }
        }

        companion object {
            fun getByDepartment(departement: Departement?): Region? {
                if (departement == null) return null
                return Region.values().firstOrNull { it.departements.contains(departement) }
            }
        }
    }

    enum class Departement(override val value: String, val codePostal: String) : Territoire {
        AIN("Ain", "01"),
        ALLIER("Allier", "03"),
        ARDECHE("Ardèche", "07"),
        CANTAL("Cantal", "15"),
        DROME("Drôme", "26"),
        ISERE("Isère", "38"),
        LOIRE("Loire", "42"),
        HAUTE_LOIRE("Haute-Loire", "43"),
        PUY_DE_DOME("Puy-de-Dôme", "63"),
        RHONE("Rhône", "69"),
        SAVOIE("Savoie", "73"),
        HAUTE_SAVOIE("Haute-Savoie", "74"),
        COTE_D_OR("Côte-d’Or", "21"),
        DOUBS("Doubs", "25"),
        JURA("Jura", "39"),
        NIEVRE("Nièvre", "58"),
        HAUTE_SAONE("Haute-Saône", "70"),
        SAONE_ET_LOIRE("Saône-et-Loire", "71"),
        YONNE("Yonne", "89"),
        TERRITOIRE_DE_BELFORT("Territoire de Belfort", "90"),
        COTES_D_ARMOR("Côtes-d’Armor", "22"),
        FINISTERE("Finistère", "29"),
        ILLE_ET_VILAINE("Ille-et-Vilaine", "35"),
        MORBIHAN("Morbihan", "56"),
        CHER("Cher", "18"),
        EURE_ET_LOIR("Eure-et-Loir", "28"),
        INDRE("Indre", "36"),
        INDRE_ET_LOIRE("Indre-et-Loire", "37"),
        LOIR_ET_CHER("Loir-et-Cher", "41"),
        LOIRET("Loiret", "45"),
        CORSE_DU_SUD("Corse-du-Sud", "2A"),
        HAUTE_CORSE("Haute-Corse", "2B"),
        ARDENNES("Ardennes", "08"),
        AUBE("Aube", "10"),
        MARNE("Marne", "51"),
        HAUTE_MARNE("Haute-Marne", "52"),
        MEURTHE_ET_MOSELLE("Meurthe-et-Moselle", "54"),
        MEUSE("Meuse", "55"),
        MOSELLE("Moselle", "57"),
        BAS_RHIN("Bas-Rhin", "67"),
        HAUT_RHIN("Haut-Rhin", "68"),
        VOSGES("Vosges", "88"),
        AISNE("Aisne", "02"),
        NORD("Nord", "59"),
        OISE("Oise", "60"),
        PAS_DE_CALAIS("Pas-de-Calais", "62"),
        SOMME("Somme", "80"),
        PARIS("Paris", "75"),
        SEINE_ET_MARNE("Seine-et-Marne", "77"),
        YVELINES("Yvelines", "78"),
        ESSONNE("Essonne", "91"),
        HAUTS_DE_SEINE("Hauts-de-Seine", "92"),
        SEINE_SAINT_DENIS("Seine-Saint-Denis", "93"),
        VAL_DE_MARNE("Val-de-Marne", "94"),
        VAL_D_OISE("Val-d’Oise", "95"),
        EURE("Eure", "27"),
        MANCHE("Manche", "50"),
        ORNE("Orne", "61"),
        SEINE_MARITIME("Seine-Maritime", "76"),
        CHARENTE("Charente", "16"),
        CHARENTE_MARITIME("Charente-Maritime", "17"),
        CORREZE("Corrèze", "19"),
        CREUSE("Creuse", "23"),
        DORDOGNE("Dordogne", "24"),
        GIRONDE("Gironde", "33"),
        LANDES("Landes", "40"),
        LOT_ET_GARONNE("Lot-et-Garonne", "47"),
        CALVADOS("Calvados", "14"),
        PYRENEES_ATLANTIQUES("Pyrénées-Atlantiques", "64"),
        DEUX_SEVRES("Deux-Sèvres", "79"),
        VIENNE("Vienne", "86"),
        HAUTE_VIENNE("Haute-Vienne", "87"),
        ARIEGE("Ariège", "09"),
        AUDE("Aude", "11"),
        AVEYRON("Aveyron", "12"),
        GARD("Gard", "30"),
        HAUTE_GARONNE("Haute-Garonne", "31"),
        GERS("Gers", "32"),
        HERAULT("Hérault", "34"),
        LOT("Lot", "46"),
        LOZERE("Lozère", "48"),
        HAUTES_PYRENEES("Hautes-Pyrénées", "65"),
        PYRENEES_ORIENTALES("Pyrénées-Orientales", "66"),
        TARN("Tarn", "81"),
        TARN_ET_GARONNE("Tarn-et-Garonne", "82"),
        LOIRE_ATLANTIQUE("Loire-Atlantique", "44"),
        MAINE_ET_LOIRE("Maine-et-Loire", "49"),
        MAYENNE("Mayenne", "53"),
        SARTHE("Sarthe", "72"),
        VENDEE("Vendée", "85"),
        ALPES_DE_HAUTE_PROVENCE("Alpes-de-Haute-Provence", "04"),
        HAUTES_ALPES("Hautes-Alpes", "05"),
        ALPES_MARITIMES("Alpes-Maritimes", "06"),
        BOUCHES_DU_RHONE("Bouches-du-Rhône", "13"),
        VAR("Var", "83"),
        VAUCLUSE("Vaucluse", "84"),
        GUADELOUPE("Guadeloupe", "971"),
        MARTINIQUE("Martinique", "972"),
        GUYANE("Guyane", "973"),
        LA_REUNION("La Réunion", "974"),
        MAYOTTE("Mayotte", "976"),
        SAINT_PIERRE_ET_MIQUELON("Saint-Pierre-et-Miquelon", "975"),
        SAINT_BARTHELEMY("Saint-Barthélémy", "977"),
        SAINT_MARTIN("Saint-Martin", "978"),
        TERRES_AUSTRALES_ET_ANTARCTIQUES_FRANCAISES("Terres australes et antarctiques françaises", "984"),
        WALLIS_ET_FUTUNA("Wallis-et-Futuna", "986"),
        POLYNESIE_FRANCAISE("Polynésie française", "987"),
        NOUVELLE_CALEDONIE("Nouvelle-Calédonie", "988");

        companion object {
            fun fromOrThrow(departement: String): Departement {
                Departement.values().firstOrNull { it.value.lowercase() == departement.lowercase() }
                    ?.let { return it }
                    ?: throw InvalidTerritoryException(departement)
            }

            fun from(departement: String): Departement? {
                Departement.values().firstOrNull { it.value.lowercase() == departement.lowercase() }
                    .let { return it }
            }
        }
    }
}

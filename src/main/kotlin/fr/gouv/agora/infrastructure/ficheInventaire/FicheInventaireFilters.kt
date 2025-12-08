package fr.gouv.agora.infrastructure.ficheInventaire

data class FicheInventaireFilters (
    val titreParam: String? = null,
    val thematiqueParam: String? = null,
    val etapeParam: List<String>? = null,
    val conditionParticipationParam: List<String>? = null,
    val modaliteParticipationParam: List<String>? = null,
    val anneeDeLancementParam: String? = null,
) {
    val titre = titreParam?.takeIf { it.isNotBlank() }
    val thematique= thematiqueParam?.takeIf { it.isNotBlank() }

    val etape: List<String>? = etapeParam
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?.takeIf { it.isNotEmpty() }

    val conditionParticipation: List<String>? = conditionParticipationParam
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?.takeIf { it.isNotEmpty() }

    val modaliteParticipation: List<String>? = modaliteParticipationParam
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?.takeIf { it.isNotEmpty() }

    val anneeDeLancement = anneeDeLancementParam?.takeIf { it.isNotBlank() }
}

package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire

interface FicheInventaireRepository {
    fun getAll(thematique: String?, etape: String?, modalite_participation: List<String>?): List<FicheInventaire>
    fun get(id: String): FicheInventaire?
}

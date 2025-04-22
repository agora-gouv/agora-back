package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire

interface FicheInventaireRepository {
    fun getAll(): List<FicheInventaire>
    fun get(id: String): FicheInventaire?
}

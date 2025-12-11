package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import fr.gouv.agora.infrastructure.ficheInventaire.FicheInventaireFilters

interface FicheInventaireRepository {
    fun getAll(filters: FicheInventaireFilters): List<FicheInventaire>
    fun get(id: String): FicheInventaire?
}

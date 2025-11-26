package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import fr.gouv.agora.domain.Thematique

interface FicheInventaireRepository {
    fun getAll(thematique: String?): List<FicheInventaire>
    fun get(id: String): FicheInventaire?
}

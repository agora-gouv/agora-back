package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire

interface FicheInventaireRepository {
    fun getAll(thematique: String?, etape: String?, modaliteParticipation: List<String>?, anneeDeLancement: String?): List<FicheInventaire>
    fun get(id: String): FicheInventaire?
}

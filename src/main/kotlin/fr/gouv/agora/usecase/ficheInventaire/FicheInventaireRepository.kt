package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire

interface FicheInventaireRepository {
    fun getAll(titre: String?, thematique: String?, etape: List<String>?, conditionParticipation: List<String>?, modaliteParticipation: List<String>?, anneeDeLancement: String?): List<FicheInventaire>
    fun get(id: String): FicheInventaire?
}

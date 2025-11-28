package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import org.springframework.stereotype.Service

@Service
class GetFichesInventaireUseCase(
    private val ficheInventaireRepository: FicheInventaireRepository
) {
    fun execute(titre: String?, thematique: String?, etape: String?, modaliteParticipation: List<String>?, anneeDeLancement: String?): List<FicheInventaire> {
        return ficheInventaireRepository.getAll(titre, thematique, etape, modaliteParticipation, anneeDeLancement )
    }
}

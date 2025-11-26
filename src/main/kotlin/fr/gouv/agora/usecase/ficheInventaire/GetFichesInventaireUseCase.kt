package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import org.springframework.stereotype.Service

@Service
class GetFichesInventaireUseCase(
    private val ficheInventaireRepository: FicheInventaireRepository
) {
    fun execute(thematique: String?, etape: String?, modalite_participation: List<String>?): List<FicheInventaire> {
        return ficheInventaireRepository.getAll(thematique, etape, modalite_participation)
    }
}

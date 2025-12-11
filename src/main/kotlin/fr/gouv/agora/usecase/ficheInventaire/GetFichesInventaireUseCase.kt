package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import fr.gouv.agora.infrastructure.ficheInventaire.FicheInventaireFilters
import org.springframework.stereotype.Service

@Service
class GetFichesInventaireUseCase(
    private val ficheInventaireRepository: FicheInventaireRepository
) {
    fun execute(filters: FicheInventaireFilters): List<FicheInventaire> {
        return ficheInventaireRepository.getAll(filters )
    }
}

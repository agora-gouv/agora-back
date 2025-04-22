package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import org.springframework.stereotype.Service

@Service
class GetFichesInventaireUseCase(
    private val ficheInventaireRepository: FicheInventaireRepository
) {
    fun execute(): List<FicheInventaire> {
        return ficheInventaireRepository.getAll()
    }
}

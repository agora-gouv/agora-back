package fr.gouv.agora.usecase.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import org.springframework.stereotype.Service

@Service
class GetFicheInventaireUseCase(
    private val ficheInventaireRepository: FicheInventaireRepository
) {
    fun execute(id: String): FicheInventaire {
        val ficheInventaire = ficheInventaireRepository.get(id)
            ?: throw FicheInventaireNotFound(id)

        return ficheInventaire
    }
}

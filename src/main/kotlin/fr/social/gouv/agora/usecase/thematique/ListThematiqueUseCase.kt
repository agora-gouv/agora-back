package fr.social.gouv.agora.usecase.thematique

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ListThematiqueUseCase(private val thematiqueRepository: ThematiqueRepository) {

    fun getThematiqueList(): List<Thematique> {
        val listThematiqueBeforeSort = thematiqueRepository.getThematiqueList()
        return listThematiqueBeforeSort.sortedWith(compareBy({ it.label == "Autre" }, { it.label }))
    }
}

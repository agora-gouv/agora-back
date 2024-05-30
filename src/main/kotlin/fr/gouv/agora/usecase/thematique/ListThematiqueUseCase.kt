package fr.gouv.agora.usecase.thematique

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ListThematiqueUseCase(private val thematiqueRepository: ThematiqueRepository) {
    companion object {
        private const val ID_THEMATIQUE_AUTRE = "47897e51-8e94-4920-a26a-1b1e5e232e82"
    }

    fun getThematiqueList(): List<Thematique> {
        val listThematiqueBeforeSort = thematiqueRepository.getThematiqueList()

        // todo supprimer la moitié ici ? -> faire la migration des ids de thématiques de la db ?
        // todo -> si les ids de thématiques sont les mêmes dans les relations, on est booooon
        return listThematiqueBeforeSort.sortedWith(compareBy({ it.id == ID_THEMATIQUE_AUTRE }, { it.label }))
    }
}

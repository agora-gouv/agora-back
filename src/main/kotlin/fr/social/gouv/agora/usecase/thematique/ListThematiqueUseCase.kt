package fr.social.gouv.agora.usecase.thematique

import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ListThematiqueUseCase(private val thematiqueRepository: ThematiqueRepository) {

    fun getThematiqueList() = thematiqueRepository.getThematiqueList()

}

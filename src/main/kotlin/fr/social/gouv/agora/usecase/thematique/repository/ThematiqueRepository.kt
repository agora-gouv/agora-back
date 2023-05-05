package fr.social.gouv.agora.usecase.thematique.repository

import fr.social.gouv.agora.domain.Thematique

interface ThematiqueRepository {
    fun getThematiqueList(): List<Thematique>
    fun getThematique(thematiqueId: String): Thematique?
}

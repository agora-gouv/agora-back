package fr.gouv.agora.usecase.thematique.repository

import fr.gouv.agora.domain.Thematique

interface ThematiqueRepository {
    fun getThematiqueList(): List<Thematique>
    fun getThematique(thematiqueId: String): Thematique?
}

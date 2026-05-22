package fr.gouv.agora.usecase.themeHebdo.repository

import fr.gouv.agora.domain.ThemeHebdo

interface ThemeHebdoRepository {
    fun getThemeHebdoList(): List<ThemeHebdo>
}

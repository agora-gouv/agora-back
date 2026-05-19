package fr.gouv.agora.usecase.themeHebdo.repository

import fr.gouv.agora.domain.ThematiqueHebdo

interface ThemeHebdoRepository {
    fun getThemeHebdo(): ThematiqueHebdo?
}

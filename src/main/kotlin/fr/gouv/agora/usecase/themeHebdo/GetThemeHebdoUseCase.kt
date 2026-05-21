package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThematiqueHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import org.springframework.stereotype.Service

@Service
class GetThemeHebdoUseCase(private val themeHebdoRepository: ThemeHebdoRepository) {

    fun getThemeHebdo(): ThematiqueHebdo? {
        return themeHebdoRepository.getThemeHebdoList().firstOrNull()
    }
}

package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import org.springframework.stereotype.Service

@Service
class GetThemeHebdoUseCase(private val themeHebdoRepository: ThemeHebdoRepository) {

    fun getThemeHebdo(): ThemeHebdo? {
        var result =  themeHebdoRepository.getThemeHebdoList().firstOrNull()
        if (result != null) {
            result.titre = "Cette semaine"
            result.sousTitre ="Cette semaine posez vos questions sur..."
            result.titreCompteur = "Cloture des votes"
        }
        return result
    }
}

package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Service

@Service
class FicheInventaireJsonMapper(
    private val dateMapper: DateMapper,
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
) {
    fun toFicheInventaireJson(fichesInventaire: List<FicheInventaire>): List<FicheInventaireJson> {
        return fichesInventaire.map {
            FicheInventaireJson(
                etapeLancementHtml = it.etapeLancement,
                etapeAnalyseHtml = it.etapeAnalyse,
                etapeSuiviHtml = it.etapeSuivi,
                titre = it.titre,
                debut = dateMapper.toFormattedDate(it.debut),
                fin = dateMapper.toFormattedDate(it.fin),
                porteur = it.porteur,
                lienSite = it.lienSite,
                conditionParticipation = it.conditionParticipation,
                modaliteParticipation = it.modaliteParticipation,
                objectif = it.objectif,
                thematique = thematiqueJsonMapper.toNoIdJson(it.thematique),
            )
        }
    }
}

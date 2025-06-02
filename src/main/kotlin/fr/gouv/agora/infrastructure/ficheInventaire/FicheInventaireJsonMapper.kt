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
    fun toFicheInventaireJson(fiche: FicheInventaire): FicheInventaireJson {
        return FicheInventaireJson(
            id = fiche.id,
            etapeLancementHtml = fiche.etapeLancement,
            etapeAnalyseHtml = fiche.etapeAnalyse,
            etapeSuiviHtml = fiche.etapeSuivi,
            titre = fiche.titre,
            debut = dateMapper.toFormattedDate(fiche.debut),
            fin = dateMapper.toFormattedDate(fiche.fin),
            porteur = fiche.porteur,
            lienSite = fiche.lienSite,
            conditionParticipation = fiche.conditionParticipation,
            modaliteParticipation = fiche.modaliteParticipation,
            thematique = thematiqueJsonMapper.toNoIdJson(fiche.thematique),
            illustrationUrl = fiche.illustration,
            etape = fiche.etape,
            anneeDeLancement = fiche.anneeDeLancement,
            type = fiche.type,
        )
    }
}

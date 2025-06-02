package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import fr.gouv.agora.usecase.ficheInventaire.FicheInventaireRepository
import org.springframework.stereotype.Repository

@Repository
class FicheInventaireRepositoryImpl(
    private val ficheInventaireStrapiRepository: FicheInventaireStrapiRepository,
    private val thematiqueMapper: ThematiqueMapper,
): FicheInventaireRepository {
    override fun getAll(): List<FicheInventaire> {
        return ficheInventaireStrapiRepository.getFichesInventaire().data
            .map { toFicheInventaire(it) }
    }

    override fun get(id: String): FicheInventaire? {
        val ficheInventaire = ficheInventaireStrapiRepository.getFicheInventaire(id)
            ?: return null
        return toFicheInventaire(ficheInventaire)
    }

    private fun toFicheInventaire(fiche: StrapiAttributes<FicheInventaireStrapiDTO>): FicheInventaire {
        return FicheInventaire(
            id = fiche.id,
            etapeLancement = fiche.attributes.etapeLancement.toHtml(),
            etapeAnalyse = fiche.attributes.etapeAnalyse.toHtml(),
            etapeSuivi = fiche.attributes.etapeSuivi.toHtml(),
            titre = fiche.attributes.titre,
            debut = fiche.attributes.debut,
            fin = fiche.attributes.fin,
            porteur = fiche.attributes.porteur,
            lienSite = fiche.attributes.lienSite,
            conditionParticipation = fiche.attributes.conditionParticipation,
            modaliteParticipation = fiche.attributes.modaliteParticipation,
            thematique = thematiqueMapper.toDomain(fiche.attributes.thematique),
            illustration = fiche.attributes.illustration.data.attributes.mediaUrl(),
            etape = fiche.attributes.etape,
            anneeDeLancement = fiche.attributes.anneeDeLancement,
            type = fiche.attributes.type,
        )
    }
}

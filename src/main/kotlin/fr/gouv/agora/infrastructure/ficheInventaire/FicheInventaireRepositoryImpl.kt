package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import fr.gouv.agora.usecase.ficheInventaire.FicheInventaireRepository
import org.springframework.stereotype.Repository

@Repository
class FicheInventaireRepositoryImpl(
    private val ficheInventaireStrapiRepository: FicheInventaireStrapiRepository,
    private val thematiqueMapper: ThematiqueMapper,
): FicheInventaireRepository {
    override fun getAll(filters: FicheInventaireFilters): List<FicheInventaire> {
        return ficheInventaireStrapiRepository.getFichesInventaire(filters).data
            .map { toFicheInventaire(it) }
    }

    override fun get(id: String): FicheInventaire? {
        val ficheInventaire = ficheInventaireStrapiRepository.getFicheInventaire(id)
            ?: return null
        return toFicheInventaire(ficheInventaire)
    }

    private fun toFicheInventaire(fiche: FicheInventaireStrapiDTO): FicheInventaire {
        return FicheInventaire(
            id = fiche.documentId,
            etapeLancement = fiche.etapeLancement.toHtml(),
            etapeAnalyse = fiche.etapeAnalyse.toHtml(),
            etapeSuivi = fiche.etapeSuivi.toHtml(),
            titre = fiche.titre,
            debut = fiche.debut,
            fin = fiche.fin,
            porteur = fiche.porteur,
            lienSite = fiche.lienSite,
            conditionParticipation = fiche.conditionParticipation,
            modaliteParticipation = fiche.modaliteParticipation,
            thematique = thematiqueMapper.toDomain(fiche.thematique),
            illustration = fiche.illustration.mediaUrl(),
            etape = fiche.etape,
            anneeDeLancement = fiche.anneeDeLancement,
            type = fiche.type,
        )
    }
}

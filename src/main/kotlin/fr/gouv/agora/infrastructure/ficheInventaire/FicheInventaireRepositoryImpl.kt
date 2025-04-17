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
    override fun getAll(): List<FicheInventaire> {
        return ficheInventaireStrapiRepository.getFichesInventaire().data
            .map { FicheInventaire(
                etapeLancement = it.attributes.etapeLancement.toHtml(),
                etapeAnalyse = it.attributes.etapeAnalyse.toHtml(),
                etapeSuivi = it.attributes.etapeSuivi.toHtml(),
                titre = it.attributes.titre,
                debut = it.attributes.debut,
                fin = it.attributes.fin,
                porteur = it.attributes.porteur,
                lienSite = it.attributes.lienSite,
                conditionParticipation = it.attributes.conditionParticipation,
                modaliteParticipation = it.attributes.modaliteParticipation,
                objectif = it.attributes.objectif,
                thematique = thematiqueMapper.toDomain(it.attributes.thematique),
            ) }
    }
}

package fr.gouv.agora.infrastructure.content.repository

import fr.gouv.agora.domain.SiteVitrineAccueilContent
import fr.gouv.agora.domain.SiteVitrineQuestionAuGouvernementContent
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.usecase.content.repository.ContentRepository
import org.springframework.stereotype.Repository

@Repository
class ContentRepositoryImpl(
    val contentStrapiRepository: ContentStrapiRepository,
) : ContentRepository {
    override fun getPagePoserMaQuestion(): String {
        return contentStrapiRepository.getPagePoserMaQuestion().attributes.texteRegles.toHtml()
    }

    override fun getPageQuestionsAuGouvernement(): String {
        return contentStrapiRepository.getPageQuestionsAuGouvernement().attributes.informationBottomsheet
    }

    override fun getPageReponseAuxQuestionsAuGouvernement(): String {
        return contentStrapiRepository.getPageReponseAuxQaG().attributes.informationReponseAVenirBottomsheet
    }

    override fun getPageSiteVitrineAccueil(): SiteVitrineAccueilContent {
        return contentStrapiRepository.getPageSiteVitrineAccueil().attributes.let {
            SiteVitrineAccueilContent(
                it.titreHeader,
                it.sousTitreHeader,
                it.titreBody,
                it.descriptionBody,
                it.texteImage1.toHtml(),
                it.texteImage2.toHtml(),
                it.texteImage3.toHtml(),
            )
        }
    }

    override fun getPageSiteVitrineConditionGenerales(): String {
        return contentStrapiRepository.getPageSiteVitrineConditionGenerales().attributes.conditionsGeneralesDUtilisation.toHtml()
    }

    override fun getPageSiteVitrineConsultation(): String {
        return contentStrapiRepository.getPageSiteVitrineConsultation().attributes.donnezVotreAvis.toHtml()
    }

    override fun getPageSiteVitrineDeclarationAccessibilite(): String {
        return contentStrapiRepository.getPageSiteVitrineDeclarationAccessibilite().attributes.declaration.toHtml()
    }

    override fun getPageSiteVitrineMentionsLegales(): String {
        return contentStrapiRepository.getPageSiteVitrineMentionsLegales().attributes.mentionsLegales.toHtml()
    }

    override fun getPageSiteVitrinePolitiqueConfidentialite(): String {
        return contentStrapiRepository.getPageSiteVitrinePolitiqueConfidentialite().attributes.politiqueDeConfidentialite.toHtml()
    }

    override fun getPageSiteVitrineQuestionAuGouvernement(): SiteVitrineQuestionAuGouvernementContent {
        return contentStrapiRepository.getPageSiteVitrineQuestionAuGouvernement().attributes.let {
            SiteVitrineQuestionAuGouvernementContent(
                it.titre,
                it.sousTitre,
                it.texteSoutien.toHtml(),
            )
        }
    }
}

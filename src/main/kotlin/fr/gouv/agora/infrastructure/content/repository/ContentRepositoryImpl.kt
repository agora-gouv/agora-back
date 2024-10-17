package fr.gouv.agora.infrastructure.content.repository

import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.usecase.content.repository.ContentRepository
import fr.gouv.agora.usecase.content.repository.SiteVitrineAccueilContent
import fr.gouv.agora.usecase.content.repository.SiteVitrineQuestionAuGouvernementContent
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
                it.texteImage1,
                it.texteImage2,
                it.texteImage3,
            )
        }
    }

    override fun getPageSiteVitrineConditionGenerales(): String {
        return contentStrapiRepository.getPageSiteVitrineConditionGenerales().attributes.conditionsGeneralesDUtilisation
    }

    override fun getPageSiteVitrineConsultation(): String {
        return contentStrapiRepository.getPageSiteVitrineConsultation().attributes.donnezVotreAvis
    }

    override fun getPageSiteVitrineDeclarationAccessibilite(): String {
        return contentStrapiRepository.getPageSiteVitrineDeclarationAccessibilite().attributes.declaration
    }

    override fun getPageSiteVitrineMentionsLegales(): String {
        return contentStrapiRepository.getPageSiteVitrineMentionsLegales().attributes.mentionsLegales
    }

    override fun getPageSiteVitrinePolitiqueConfidentialite(): String {
        return contentStrapiRepository.getPageSiteVitrinePolitiqueConfidentialite().attributes.politiqueDeConfidentialite
    }

    override fun getPageSiteVitrineQuestionAuGouvernement(): SiteVitrineQuestionAuGouvernementContent {
        return contentStrapiRepository.getPageSiteVitrineQuestionAuGouvernement().attributes.let {
            SiteVitrineQuestionAuGouvernementContent(
                it.titre,
                it.sousTitre,
                it.texteSoutien,
            )
        }
    }
}

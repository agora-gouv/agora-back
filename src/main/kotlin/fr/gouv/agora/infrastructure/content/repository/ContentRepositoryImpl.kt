package fr.gouv.agora.infrastructure.content.repository

import fr.gouv.agora.domain.PageQuestionAuGouvernementContent
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
        return contentStrapiRepository.getPagePoserMaQuestion().texteRegles.toHtml()
    }

    override fun getPageQuestionsAuGouvernement(): PageQuestionAuGouvernementContent {
        return contentStrapiRepository.getPageQuestionsAuGouvernement().let {
            PageQuestionAuGouvernementContent(
                it.informationBottomsheet,
                it.nombreDeQuestions,
                it.programmeDuMois?.toHtml(),
                it.commentCaMarche,
            )
        }
    }

    override fun getPageReponseAuxQuestionsAuGouvernement(): String {
        return contentStrapiRepository.getPageReponseAuxQaG().informationReponseAVenirBottomsheet
    }

    override fun getPageSiteVitrineAccueil(): SiteVitrineAccueilContent {
        return contentStrapiRepository.getPageSiteVitrineAccueil().let {
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
        return contentStrapiRepository.getPageSiteVitrineConditionGenerales().conditionsGeneralesDUtilisation.toHtml()
    }

    override fun getPageSiteVitrineConsultation(): String {
        return contentStrapiRepository.getPageSiteVitrineConsultation().donnezVotreAvis.toHtml()
    }

    override fun getPageSiteVitrineDeclarationAccessibilite(): String {
        return contentStrapiRepository.getPageSiteVitrineDeclarationAccessibilite().declaration.toHtml()
    }

    override fun getPageSiteVitrineMentionsLegales(): String {
        return contentStrapiRepository.getPageSiteVitrineMentionsLegales().mentionsLegales.toHtml()
    }

    override fun getPageSiteVitrinePolitiqueConfidentialite(): String {
        return contentStrapiRepository.getPageSiteVitrinePolitiqueConfidentialite().politiqueDeConfidentialite.toHtml()
    }

    override fun getPageSiteVitrineQuestionAuGouvernement(): SiteVitrineQuestionAuGouvernementContent {
        val dto = contentStrapiRepository.getPageSiteVitrineQuestionAuGouvernement()
        return SiteVitrineQuestionAuGouvernementContent(
            dto.titre,
            dto.sousTitre,
            dto.texteSoutien.toHtml(),
        )
    }
}

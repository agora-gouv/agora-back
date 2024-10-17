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
        TODO("Not yet implemented")
    }

    override fun getPageSiteVitrineConditionGenerales(): String {
        TODO("Not yet implemented")
    }

    override fun getPageSiteVitrineConsultation(): String {
        TODO("Not yet implemented")
    }

    override fun getPageSiteVitrineDeclarationAccessibilite(): String {
        TODO("Not yet implemented")
    }

    override fun getPageSiteVitrineMentionsLegales(): String {
        TODO("Not yet implemented")
    }

    override fun getPageSiteVitrinePolitiqueConfidentialite(): String {
        TODO("Not yet implemented")
    }

    override fun getPageSiteVitrineQuestionAuGouvernement(): SiteVitrineQuestionAuGouvernementContent {
        TODO("Not yet implemented")
    }
}

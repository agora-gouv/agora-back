package fr.gouv.agora.usecase.content.repository

import fr.gouv.agora.domain.SiteVitrineAccueilContent
import fr.gouv.agora.domain.SiteVitrineQuestionAuGouvernementContent

interface ContentRepository {
    fun getPagePoserMaQuestion(): String
    fun getPageQuestionsAuGouvernement(): String
    fun getPageReponseAuxQuestionsAuGouvernement(): String
    fun getPageSiteVitrineAccueil(): SiteVitrineAccueilContent
    fun getPageSiteVitrineConditionGenerales(): String
    fun getPageSiteVitrineConsultation(): String
    fun getPageSiteVitrineDeclarationAccessibilite(): String
    fun getPageSiteVitrineMentionsLegales(): String
    fun getPageSiteVitrinePolitiqueConfidentialite(): String
    fun getPageSiteVitrineQuestionAuGouvernement(): SiteVitrineQuestionAuGouvernementContent
}

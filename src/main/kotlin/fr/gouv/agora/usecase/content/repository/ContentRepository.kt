package fr.gouv.agora.usecase.content.repository

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

data class SiteVitrineAccueilContent(
    val titreHeader: String,
    val sousTitreHeader: String,
    val titreBody: String,
    val descriptionBody: String,
    val texteImage1: String,
    val texteImage2: String,
    val texteImage3: String,
)
data class SiteVitrineQuestionAuGouvernementContent(
    val titre: String,
    val sousTitre: String,
    val texteSoutien: String,
)

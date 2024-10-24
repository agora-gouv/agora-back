package fr.gouv.agora.infrastructure.content.json

data class QuestionsAuGouvernementContentJson(val info: String, val texteTotalQuestions: String)
data class ReponseAuxQagsJson(val infoReponsesAVenir: String)
data class PoserMaQuestionJson(val regles: String)

data class SiteVitrineAccueilJson(
    val titreHeader: String,
    val sousTitreHeader: String,
    val titreBody: String,
    val descriptionBody: String,
    val texteImage1: String,
    val texteImage2: String,
    val texteImage3: String,
)
data class SiteVitrineConditionGeneralesJson(val conditionsGeneralesDUtilisation: String)
data class SiteVitrineConsultationJson(val donnezVotreAvis: String)
data class SiteVitrineDeclarationAccessibiliteJson(val declaration: String)
data class SiteVitrineMentionsLegalesJson(val mentionsLegales: String)
data class SiteVitrinePolitiqueConfidentialiteJson(val politiqueDeConfidentialite: String)
data class SiteVitrineQuestionAuGouvernementJson(
    val titre: String,
    val sousTitre: String,
    val texteSoutien: String,
)

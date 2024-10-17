package fr.gouv.agora.infrastructure.content

import fr.gouv.agora.infrastructure.content.json.PoserMaQuestionJson
import fr.gouv.agora.infrastructure.content.json.QuestionsAuGouvernementContentJson
import fr.gouv.agora.infrastructure.content.json.ReponseAuxQagsJson
import fr.gouv.agora.infrastructure.content.json.SiteVitrineAccueilJson
import fr.gouv.agora.infrastructure.content.json.SiteVitrineConditionGeneralesJson
import fr.gouv.agora.infrastructure.content.json.SiteVitrineConsultationJson
import fr.gouv.agora.infrastructure.content.json.SiteVitrineDeclarationAccessibiliteJson
import fr.gouv.agora.infrastructure.content.json.SiteVitrineMentionsLegalesJson
import fr.gouv.agora.infrastructure.content.json.SiteVitrinePolitiqueConfidentialiteJson
import fr.gouv.agora.infrastructure.content.json.SiteVitrineQuestionAuGouvernementJson
import fr.gouv.agora.usecase.content.GetContentPagePoserMaQuestionUseCase
import fr.gouv.agora.usecase.content.GetContentPageReponseAuxQuestionsAuGouvernementUseCase
import fr.gouv.agora.usecase.content.GetContentQuestionsAuGouvernementUseCase
import fr.gouv.agora.usecase.content.GetSiteVitrineAccueilContentUseCase
import fr.gouv.agora.usecase.content.GetSiteVitrineConditionGeneralesContentUseCase
import fr.gouv.agora.usecase.content.GetSiteVitrineConsultationContentUseCase
import fr.gouv.agora.usecase.content.GetSiteVitrineDeclarationAccessibiliteContentUseCase
import fr.gouv.agora.usecase.content.GetSiteVitrineMentionsLegalesContentUseCase
import fr.gouv.agora.usecase.content.GetSiteVitrinePolitiqueConfidentialiteContentUseCase
import fr.gouv.agora.usecase.content.GetSiteVitrineQuestionAuGouvernementContentUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/content")
@Tag(name = "Content")
class ContentController(
    private val getContentPageQuestionsAuGouvernementUseCase: GetContentQuestionsAuGouvernementUseCase,
    private val getContentPageReponseAuxQuestionsAuGouvernementUseCase: GetContentPageReponseAuxQuestionsAuGouvernementUseCase,
    private val getContentPagePoserMaQuestionUseCase: GetContentPagePoserMaQuestionUseCase,
    private val getSiteVitrineAccueilContentUseCase: GetSiteVitrineAccueilContentUseCase,
    private val getSiteVitrineConditionGeneralesContentUseCase: GetSiteVitrineConditionGeneralesContentUseCase,
    private val getSiteVitrineConsultationContentUseCase: GetSiteVitrineConsultationContentUseCase,
    private val getSiteVitrineDeclarationAccessibiliteContentUseCase: GetSiteVitrineDeclarationAccessibiliteContentUseCase,
    private val getSiteVitrineMentionsLegalesContentUseCase: GetSiteVitrineMentionsLegalesContentUseCase,
    private val getSiteVitrinePolitiqueConfidentialiteContentUseCase: GetSiteVitrinePolitiqueConfidentialiteContentUseCase,
    private val getSiteVitrineQuestionAuGouvernementContentUseCase: GetSiteVitrineQuestionAuGouvernementContentUseCase,
) {
    @Operation(summary = "Récupérer les informations de la page Questions au Gouvernement")
    @GetMapping("/page-questions-au-gouvernement")
    fun getContentQuestionsAuGouvernementPage(): ResponseEntity<QuestionsAuGouvernementContentJson> {
        val content = getContentPageQuestionsAuGouvernementUseCase.execute()
        return ResponseEntity.ok().body(QuestionsAuGouvernementContentJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Réponse aux QaGs")
    @GetMapping("/page-reponses-aux-qags")
    fun getContentReponsesAVenirPage(): ResponseEntity<ReponseAuxQagsJson> {
        val content = getContentPageReponseAuxQuestionsAuGouvernementUseCase.execute()
        return ResponseEntity.ok().body(ReponseAuxQagsJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Poser ma Question")
    @GetMapping("/page-poser-ma-question")
    fun getContentPoserMaQuestionPage(): ResponseEntity<PoserMaQuestionJson> {
        val content = getContentPagePoserMaQuestionUseCase.execute()
        return ResponseEntity.ok().body(PoserMaQuestionJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Accueil du site vitrine")
    @GetMapping("/page-site-vitrine-accueil")
    fun getContentSiteVitrineAccueilPage(): ResponseEntity<SiteVitrineAccueilJson> {
        val content = getSiteVitrineAccueilContentUseCase.execute()
        return ResponseEntity.ok().body(
            SiteVitrineAccueilJson(
                content.titreHeader,
                content.sousTitreHeader,
                content.titreBody,
                content.descriptionBody,
                content.texteImage1,
                content.texteImage2,
                content.texteImage3,
            )
        )
    }

    @Operation(summary = "Récupérer les informations de la page Conditions générales d'utilisation du site vitrine")
    @GetMapping("/page-site-vitrine-conditions-generales")
    fun getContentSiteVitrineConditionGeneralesPage(): ResponseEntity<SiteVitrineConditionGeneralesJson> {
        val content = getSiteVitrineConditionGeneralesContentUseCase.execute()
        return ResponseEntity.ok().body(SiteVitrineConditionGeneralesJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Consultation du site vitrine")
    @GetMapping("/page-site-vitrine-consultation")
    fun getContentSiteVitrineConsultationPage(): ResponseEntity<SiteVitrineConsultationJson> {
        val content = getSiteVitrineConsultationContentUseCase.execute()
        return ResponseEntity.ok().body(SiteVitrineConsultationJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Déclaration d'accessibilité du site vitrine")
    @GetMapping("/page-site-vitrine-declaration-accessibilite")
    fun getContentSiteVitrineDeclarationAccessibilitePage(): ResponseEntity<SiteVitrineDeclarationAccessibiliteJson> {
        val content = getSiteVitrineDeclarationAccessibiliteContentUseCase.execute()
        return ResponseEntity.ok().body(SiteVitrineDeclarationAccessibiliteJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Mentions légales du site vitrine")
    @GetMapping("/page-site-vitrine-mentions-legales")
    fun getContentSiteVitrineMentionsLegalesPage(): ResponseEntity<SiteVitrineMentionsLegalesJson> {
        val content = getSiteVitrineMentionsLegalesContentUseCase.execute()
        return ResponseEntity.ok().body(SiteVitrineMentionsLegalesJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Politique de confidentialité du site vitrine")
    @GetMapping("/page-site-vitrine-politique-confidentialite")
    fun getContentSiteVitrinePolitiqueConfidentialitePage(): ResponseEntity<SiteVitrinePolitiqueConfidentialiteJson> {
        val content = getSiteVitrinePolitiqueConfidentialiteContentUseCase.execute()
        return ResponseEntity.ok().body(SiteVitrinePolitiqueConfidentialiteJson(content))
    }

    @Operation(summary = "Récupérer les informations de la page Question au gouvernement du site vitrine")
    @GetMapping("/page-site-vitrine-question-au-gouvernement")
    fun getContentSiteVitrineQuestionAuGouvernementPage(): ResponseEntity<SiteVitrineQuestionAuGouvernementJson> {
        val content = getSiteVitrineQuestionAuGouvernementContentUseCase.execute()
        return ResponseEntity.ok().body(
            SiteVitrineQuestionAuGouvernementJson(
                content.titre,
                content.sousTitre,
                content.texteSoutien,
            )
        )
    }
}

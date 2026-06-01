package fr.gouv.agora.e2e.content

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.content.repository.ContentStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Tests E2E Strapi — tag "Content"
 *
 * Vérifie que chaque endpoint Strapi retourne une réponse désérialisable
 * avec au moins un champ non vide.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class ContentStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = ContentStrapiRepository(cmsStrapiHttpClient)

    // ---- page-questions-au-gouvernement ----

    @Test
    fun `getPageQuestionsAuGouvernement - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageQuestionsAuGouvernement()

        assertThat(result).isNotNull
        assertThat(result.informationBottomsheet).isNotBlank
        assertThat(result.nombreDeQuestions).isNotBlank
    }

    // ---- page-reponse-aux-questions-au-gouvernement ----

    @Test
    fun `getPageReponseAuxQaG - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageReponseAuxQaG()

        assertThat(result).isNotNull
        assertThat(result.informationReponseAVenirBottomsheet).isNotBlank
    }

    // ---- page-poser-ma-question ----

    @Test
    fun `getPagePoserMaQuestion - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPagePoserMaQuestion()

        assertThat(result).isNotNull
        assertThat(result.texteRegles).isNotEmpty
    }

    // ---- site-vitrine-accueil ----

    @Test
    fun `getPageSiteVitrineAccueil - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineAccueil()

        assertThat(result).isNotNull
        assertThat(result.titreHeader).isNotBlank
        assertThat(result.sousTitreHeader).isNotBlank
        assertThat(result.titreBody).isNotBlank
    }

    // ---- site-vitrine-conditions-generales-d-utilisation ----

    @Test
    fun `getPageSiteVitrineConditionGenerales - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineConditionGenerales()

        assertThat(result).isNotNull
        assertThat(result.conditionsGeneralesDUtilisation).isNotEmpty
    }

    // ---- site-vitrine-consultation ----

    @Test
    fun `getPageSiteVitrineConsultation - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineConsultation()

        assertThat(result).isNotNull
        assertThat(result.donnezVotreAvis).isNotEmpty
    }

    // ---- site-vitrine-declaration-d-accessibilite ----

    @Test
    fun `getPageSiteVitrineDeclarationAccessibilite - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineDeclarationAccessibilite()

        assertThat(result).isNotNull
        assertThat(result.declaration).isNotEmpty
    }

    // ---- site-vitrine-mentions-legale ----

    @Test
    fun `getPageSiteVitrineMentionsLegales - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineMentionsLegales()

        assertThat(result).isNotNull
        assertThat(result.mentionsLegales).isNotEmpty
    }

    // ---- site-vitrine-politique-de-confidentialite ----

    @Test
    fun `getPageSiteVitrinePolitiqueConfidentialite - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrinePolitiqueConfidentialite()

        assertThat(result).isNotNull
        assertThat(result.politiqueDeConfidentialite).isNotEmpty
    }

    // ---- site-vitrine-question-au-gouvernement ----

    @Test
    fun `getPageSiteVitrineQuestionAuGouvernement - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineQuestionAuGouvernement()

        assertThat(result).isNotNull
        assertThat(result.titre).isNotBlank
        assertThat(result.sousTitre).isNotBlank
    }
}

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
        assertThat(result.attributes.informationBottomsheet).isNotBlank
        assertThat(result.attributes.nombreDeQuestions).isNotBlank
    }

    // ---- page-reponse-aux-questions-au-gouvernement ----

    @Test
    fun `getPageReponseAuxQaG - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageReponseAuxQaG()

        assertThat(result).isNotNull
        assertThat(result.attributes.informationReponseAVenirBottomsheet).isNotBlank
    }

    // ---- page-poser-ma-question ----

    @Test
    fun `getPagePoserMaQuestion - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPagePoserMaQuestion()

        assertThat(result).isNotNull
        assertThat(result.attributes.texteRegles).isNotEmpty
    }

    // ---- site-vitrine-accueil ----

    @Test
    fun `getPageSiteVitrineAccueil - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineAccueil()

        assertThat(result).isNotNull
        assertThat(result.attributes.titreHeader).isNotBlank
        assertThat(result.attributes.sousTitreHeader).isNotBlank
        assertThat(result.attributes.titreBody).isNotBlank
    }

    // ---- site-vitrine-conditions-generales-d-utilisation ----

    @Test
    fun `getPageSiteVitrineConditionGenerales - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineConditionGenerales()

        assertThat(result).isNotNull
        assertThat(result.attributes.conditionsGeneralesDUtilisation).isNotEmpty
    }

    // ---- site-vitrine-consultation ----

    @Test
    fun `getPageSiteVitrineConsultation - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineConsultation()

        assertThat(result).isNotNull
        assertThat(result.attributes.donnezVotreAvis).isNotEmpty
    }

    // ---- site-vitrine-declaration-d-accessibilite ----

    @Test
    fun `getPageSiteVitrineDeclarationAccessibilite - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineDeclarationAccessibilite()

        assertThat(result).isNotNull
        assertThat(result.attributes.declaration).isNotEmpty
    }

    // ---- site-vitrine-mentions-legale ----

    @Test
    fun `getPageSiteVitrineMentionsLegales - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineMentionsLegales()

        assertThat(result).isNotNull
        assertThat(result.attributes.mentionsLegales).isNotEmpty
    }

    // ---- site-vitrine-politique-de-confidentialite ----

    @Test
    fun `getPageSiteVitrinePolitiqueConfidentialite - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrinePolitiqueConfidentialite()

        assertThat(result).isNotNull
        assertThat(result.attributes.politiqueDeConfidentialite).isNotEmpty
    }

    // ---- site-vitrine-question-au-gouvernement ----

    @Test
    fun `getPageSiteVitrineQuestionAuGouvernement - should return non-empty content`() {
        skipIfNoCredentials()

        val result = repository.getPageSiteVitrineQuestionAuGouvernement()

        assertThat(result).isNotNull
        assertThat(result.attributes.titre).isNotBlank
        assertThat(result.attributes.sousTitre).isNotBlank
    }
}

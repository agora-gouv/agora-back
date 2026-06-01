package fr.gouv.agora.e2e.responseQag

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.responseQag.repository.ResponseQagStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Tests E2E Strapi — tag "ResponseQag"
 *
 * Vérifie que les endpoints Strapi reponse-du-gouvernements retournent
 * une réponse désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class ResponseQagStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = ResponseQagStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /reponse-du-gouvernements (liste paginée) ----

    @Test
    fun `getResponsesQag - should return non-empty list`() {
        skipIfNoCredentials()

        val result = repository.getResponsesQag()

        assertThat(result).isNotNull
        assertThat(result.data).isNotEmpty
        result.data.forEach { response ->
            assertThat(response.attributes.auteur).isNotBlank
            assertThat(response.attributes.questionId).isNotBlank
            assertThat(response.attributes.reponseDate).isNotNull
        }
    }

    // ---- GET /reponse-du-gouvernements (count) ----

    @Test
    fun `getResponsesCount - should return positive count`() {
        skipIfNoCredentials()

        val result = repository.getResponsesCount()

        assertThat(result).isGreaterThan(0)
    }
}

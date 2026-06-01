package fr.gouv.agora.e2e.welcomePage

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Tests E2E Strapi — tag "News"
 *
 * Vérifie que l'endpoint Strapi welcome-page-news retourne une réponse
 * désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class NewsStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = NewsStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /welcome-page-news ----

    @Test
    fun `getNews - should return non-empty list`() {
        skipIfNoCredentials()

        val result = repository.getNews()

        assertThat(result).isNotEmpty
        result.forEach { news ->
            assertThat(news.attributes.short_message).isNotBlank
            assertThat(news.attributes.callToAction).isNotBlank
            assertThat(news.attributes.pageRouteMobile).isNotBlank
        }
    }
}

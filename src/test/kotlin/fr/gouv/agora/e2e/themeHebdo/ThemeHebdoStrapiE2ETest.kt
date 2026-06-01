package fr.gouv.agora.e2e.themeHebdo

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.themeHebdo.repository.ThemeHebdoStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Tests E2E Strapi — tag "ThemeHebdo"
 *
 * Vérifie que l'endpoint Strapi theme-hebdos retourne une réponse
 * désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class ThemeHebdoStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = ThemeHebdoStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /theme-hebdos ----

    @Test
    fun `getThemeHebdo - should return non-empty list`() {
        skipIfNoCredentials()

        val result = repository.getThemeHebdo()

        assertThat(result).isNotNull
        assertThat(result.data).isNotEmpty
        result.data.forEach { themeHebdo ->
            assertThat(themeHebdo.attributes.theme).isNotBlank
            assertThat(themeHebdo.attributes.date_debut).isNotBlank
            assertThat(themeHebdo.attributes.date_fin).isNotBlank
        }
    }
}

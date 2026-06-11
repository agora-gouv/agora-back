package fr.gouv.agora.e2e.thematique

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Tests E2E Strapi — tag "Thematique"
 *
 * Vérifie que l'endpoint Strapi thematiques retourne une réponse
 * désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class ThematiqueStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = ThematiqueStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /thematiques ----

    @Test
    fun `getThematiques - should return non-empty list`() {
        skipIfNoCredentials()

        val result = repository.getThematiques()

        assertThat(result).isNotNull
        assertThat(result.data).isNotEmpty
        result.data.forEach { thematique ->
            assertThat(thematique.label).isNotBlank
            assertThat(thematique.pictogramme).isNotBlank
        }
    }
}

package fr.gouv.agora.e2e.concertations

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.concertations.ConcertationStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Tests E2E Strapi — tag "Concertation"
 *
 * Vérifie que l'endpoint Strapi concertations retourne une réponse
 * désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class ConcertationStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = ConcertationStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /concertations ----

    @Test
    fun `getAll - should return non-empty list`() {
        skipIfNoCredentials()

        val result = repository.getAll()

        assertThat(result).isNotNull
        assertThat(result.data).isNotEmpty
        result.data.forEach { concertation ->
            assertThat(concertation.attributes.titre).isNotBlank
            assertThat(concertation.attributes.urlExterne).isNotBlank
        }
    }
}

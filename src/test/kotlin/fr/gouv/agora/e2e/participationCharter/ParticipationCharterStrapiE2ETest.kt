package fr.gouv.agora.e2e.participationCharter

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.participationCharter.repository.ParticipationCharterStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * Tests E2E Strapi — tag "ParticipationCharter"
 *
 * Vérifie que l'endpoint Strapi charte-participations retourne une réponse
 * désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class ParticipationCharterStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = ParticipationCharterStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /charte-participations ----

    @Test
    fun `getLastParticipationCharter - should return non-empty charter`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val result = repository.getLastParticipationCharter(now)

        assertThat(result).isNotNull
        assertThat(result.charte).isNotEmpty
        assertThat(result.datetimeDebut).isNotNull
    }
}

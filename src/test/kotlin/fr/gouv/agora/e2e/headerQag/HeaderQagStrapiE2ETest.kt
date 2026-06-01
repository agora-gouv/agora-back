package fr.gouv.agora.e2e.headerQag

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.headerQag.repository.HeaderQagStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * Tests E2E Strapi — tag "HeaderQag"
 *
 * Vérifie que l'endpoint Strapi qa-g-headers-onglets retourne une réponse
 * désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class HeaderQagStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = HeaderQagStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /qa-g-headers-onglets ----

    @Test
    fun `getLastHeader - when type TOUTES - should return non-null header with non-blank fields`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val result = repository.getLastHeader("top", now)

        assertThat(result).isNotNull
        assertThat(result!!.attributes.titre).isNotBlank
        assertThat(result.attributes.message).isNotBlank
        assertThat(result.attributes.type).isNotBlank
    }

    @Test
    fun `getLastHeader - when type POPULAIRES - should return non-null header with non-blank fields`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val result = repository.getLastHeader("top", now)

        assertThat(result).isNotNull
        assertThat(result!!.attributes.titre).isNotBlank
        assertThat(result.attributes.message).isNotBlank
    }
}

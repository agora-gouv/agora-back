package fr.gouv.agora.e2e.ficheInventaire

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.ficheInventaire.FicheInventaireFilters
import fr.gouv.agora.infrastructure.ficheInventaire.FicheInventaireStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Tests E2E Strapi — tag "FicheInventaire"
 *
 * Vérifie que les endpoints Strapi fiche-inventaires retournent
 * une réponse désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class FicheInventaireStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = FicheInventaireStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /fiches_inventaire (liste) ----

    @Test
    fun `getFichesInventaire - when no filters - should return non-empty list`() {
        skipIfNoCredentials()

        val emptyFilters = FicheInventaireFilters()

        val result = repository.getFichesInventaire(emptyFilters)

        assertThat(result).isNotNull
        assertThat(result.data).isNotEmpty
        result.data.forEach { fiche ->
            assertThat(fiche.titre).isNotBlank
            assertThat(fiche.porteur).isNotBlank
        }
    }

    // ---- GET /fiches_inventaire/{id} (détail par id) ----

    @Test
    fun `getFicheInventaire - when valid first id from list - should return matching fiche`() {
        skipIfNoCredentials()

        val emptyFilters = FicheInventaireFilters()

        val liste = repository.getFichesInventaire(emptyFilters)
        assertThat(liste.data).isNotEmpty

        val firstId = liste.data.first().documentId
        val result = repository.getFicheInventaire(firstId)

        assertThat(result).isNotNull
        assertThat(result!!.titre).isNotBlank
        assertThat(result.documentId).isEqualTo(firstId)
    }
}

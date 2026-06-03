package fr.gouv.agora.e2e.consultation

import fr.gouv.agora.e2e.StrapiE2ETestBase
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * Tests E2E Strapi — tag "Consultation"
 *
 * Vérifie que les endpoints Strapi consultations retournent une réponse
 * désérialisable avec des données cohérentes.
 *
 * Lancement : ./gradlew test -Pe2e
 * (skippés automatiquement si CMS_AUTH_TOKEN / CMS_API_URL non définis)
 */
@Tag("e2e")
class ConsultationStrapiE2ETest : StrapiE2ETestBase() {

    private val repository = ConsultationStrapiRepository(cmsStrapiHttpClient)

    // ---- GET /consultations (en cours avec non-publiées) ----

    @Test
    fun `getConsultationsOngoingWithUnpublished - when no territory filter - should return deserialisable list`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val result = repository.getConsultationsOngoingWithUnpublished(now, emptyList())

        assertThat(result).isNotNull
        result.data.forEach { consultation ->
            assertThat(consultation.titre).isNotBlank
            assertThat(consultation.slug).isNotBlank
        }
    }

    // ---- GET /consultations (terminées) ----

    @Test
    fun `getConsultationsFinishedWithUnpublished - when no territory filter - should return non-empty list`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val result = repository.getConsultationsFinishedWithUnpublished(now, emptyList())

        assertThat(result).isNotNull
        assertThat(result.data).isNotEmpty
        result.data.forEach { consultation ->
            assertThat(consultation.titre).isNotBlank
        }
    }

    // ---- GET /consultations (par id) ----

    @Test
    fun `getConsultationById - when valid first id from finished list - should return matching consultation`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val liste = repository.getConsultationsFinishedWithUnpublished(now, emptyList())
        assertThat(liste.data).isNotEmpty

        val firstId = liste.data.first().documentId
        val result = repository.getConsultationById(firstId)

        assertThat(result).isNotNull
        assertThat(result!!.titre).isNotBlank
        assertThat(result.documentId).isEqualTo(firstId)
    }

    // ---- isConsultationExists ----

    @Test
    fun `isConsultationExists - when valid existing id - should return true`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val liste = repository.getConsultationsFinishedWithUnpublished(now, emptyList())
        assertThat(liste.data).isNotEmpty

        val firstId = liste.data.first().documentId
        val result = repository.isConsultationExists(firstId)

        assertThat(result).isTrue()
    }

    // ---- countFinishedConsultations ----

    @Test
    fun `countFinishedConsultations - should return positive count`() {
        skipIfNoCredentials()

        val now = LocalDateTime.now()
        val result = repository.countFinishedConsultations(now)

        assertThat(result).isGreaterThan(0)
    }
}

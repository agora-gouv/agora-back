package fr.gouv.agora.e2e

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import fr.gouv.agora.config.CmsStrapiHttpClient
import org.junit.jupiter.api.Assumptions.assumeTrue
import java.net.http.HttpClient

/**
 * Base commune pour les tests E2E Strapi.
 *
 * Ces tests appellent le vrai Strapi (CMS_API_URL + CMS_AUTH_TOKEN).
 * Ils sont exclus de `./gradlew test` par défaut et se lancent uniquement avec :
 *   ./gradlew test -Pe2e
 *
 * Si CMS_AUTH_TOKEN ou CMS_API_URL est absent, les tests sont automatiquement skippés.
 */
abstract class StrapiE2ETestBase {

    companion object {
        val cmsAuthToken: String? = System.getenv("CMS_AUTH_TOKEN")
        val cmsApiUrl: String? = System.getenv("CMS_API_URL")
    }

    val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(kotlinModule())
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val cmsStrapiHttpClient: CmsStrapiHttpClient = CmsStrapiHttpClient(
        httpClient = HttpClient.newHttpClient(),
        objectMapper = objectMapper,
    )

    /**
     * À appeler au début de chaque test pour skipper si les credentials Strapi ne sont pas disponibles.
     */
    fun skipIfNoCredentials() {
        assumeTrue(
            !cmsAuthToken.isNullOrBlank() && !cmsApiUrl.isNullOrBlank(),
            "Test E2E skippé : CMS_AUTH_TOKEN ou CMS_API_URL non définis"
        )
    }
}

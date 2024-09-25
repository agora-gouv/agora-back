package fr.gouv.agora.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class CmsStrapiHttpClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper,
) {
    private val logger: Logger = LoggerFactory.getLogger(CmsStrapiHttpClient::class.java)

    fun <T> request(builder: StrapiRequestBuilder, typeReference: TypeReference<*>): StrapiDTO<T> {
        return try {
            val uri = builder.build()
            val request = getClientRequest(uri).GET().build()
            val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            logger.debug("requête Strapi vers {}", request)

            objectMapper.readValue(httpResponse.body(), typeReference) as StrapiDTO<T>
        } catch (e: Exception) {
            logger.error("Erreur lors de la requête du builder $builder: ", e)

            StrapiDTO.ofEmpty()
        }
    }

    private fun getClientRequest(uri: String): HttpRequest.Builder {
        val authToken = System.getenv("CMS_AUTH_TOKEN")
        val apiUrl = System.getenv("CMS_API_URL")
        logger.debug("Requête Strapi vers l'URI : $uri")

        return HttpRequest.newBuilder()
            .uri(URI("$apiUrl$uri"))
            .setHeader("Authorization", "Bearer $authToken")
    }
}

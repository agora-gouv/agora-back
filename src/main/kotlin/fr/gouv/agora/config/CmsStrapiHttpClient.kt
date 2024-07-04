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

    fun getAll(cmsModel: String): String {
        val uri = "${cmsModel}?populate=*&pagination[pageSize]=100"

        val request = getClientRequest(uri).GET().build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
    }

    fun <T> request(builder: StrapiRequestBuilder): StrapiDTO<T> {
        val uri = builder.build()

        return try {
            val request = getClientRequest(uri).GET().build()
            val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            val ref = object : TypeReference<StrapiDTO<T>>() {}
            objectMapper.readValue(httpResponse.body(), ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la requête $uri: ", e)

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

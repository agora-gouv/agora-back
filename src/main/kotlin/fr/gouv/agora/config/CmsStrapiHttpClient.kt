package fr.gouv.agora.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.common.StrapiSingleTypeDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class CmsStrapiHttpClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper,
    @Value("\${strapi.suspended:false}") private val strapiSuspended: Boolean,
    @Value("\${cms.auth.token:}") private val cmsAuthToken: String,
    @Value("\${cms.api.url:}") private val cmsApiUrl: String,
) {
    private val logger: Logger = LoggerFactory.getLogger(CmsStrapiHttpClient::class.java)

    fun <T> request(builder: StrapiRequestBuilder, typeReference: TypeReference<*>): StrapiDTO<T> {
        if (isStrapiSuspended()) {
            logger.warn("Trafic Strapi suspendu (STRAPI_SUSPENDED=true) — requête ignorée : $builder")
            return StrapiDTO.ofEmpty()
        }

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

    fun <T> requestSingleType(builder: StrapiRequestBuilder, typeReference: TypeReference<*>): StrapiSingleTypeDTO<T> {
        if (isStrapiSuspended()) {
            logger.warn("Trafic Strapi suspendu (STRAPI_SUSPENDED=true) — requête ignorée : $builder")
            throw StrapiTrafficSuspendedException()
        }

        return try {
            val uri = builder.build()
            val request = getClientRequest(uri).GET().build()
            val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            logger.debug("requête Strapi vers {}", request)

            objectMapper.readValue(httpResponse.body(), typeReference) as StrapiSingleTypeDTO<T>
        } catch (e: Exception) {
            logger.error("Erreur lors de la requête du builder $builder: ", e)
            throw e
        }
    }

    private fun isStrapiSuspended(): Boolean = strapiSuspended

    private fun getClientRequest(uri: String): HttpRequest.Builder {
        val uriWithoutSpace = uri.replace(" ", "%20")
        logger.info("Requête Strapi vers l'URI : $uriWithoutSpace")

        return HttpRequest.newBuilder()
            .uri(URI("$cmsApiUrl$uriWithoutSpace"))
            .setHeader("Authorization", "Bearer $cmsAuthToken")
    }
}

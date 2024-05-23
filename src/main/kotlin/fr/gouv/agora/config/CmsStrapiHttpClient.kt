package fr.gouv.agora.config

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
) {
    private val logger: Logger = LoggerFactory.getLogger(CmsStrapiHttpClient::class.java)

    fun getByIds(cmsModel: String, idField: String, ids: List<String>): String {
        if (ids.size > 100) logger.warn("attention : ne peut pas gérer plus de ~100 filtres dans l'url")

        val idsFilter = ids.map { "&filters[$idField][\$in]=$it" }.joinToString("")
        val uri = "${cmsModel}?populate=*$idsFilter"

        val request = getClientRequest(uri).GET().build()

        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
    }

    private fun getClientRequest(uri: String): HttpRequest.Builder {
        val authToken = System.getenv("CMS_AUTH_TOKEN")
        val apiUrl = System.getenv("CMS_API_URL")

        return HttpRequest.newBuilder()
            .uri(URI("$apiUrl$uri"))
            .setHeader("Authorization", "Bearer $authToken")
    }
}

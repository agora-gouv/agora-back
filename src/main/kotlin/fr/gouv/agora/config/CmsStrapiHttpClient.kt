package fr.gouv.agora.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class CmsStrapiHttpClient(
    private val httpClient: HttpClient,
) {
    private val logger: Logger = LoggerFactory.getLogger(CmsStrapiHttpClient::class.java)

    fun getBy(cmsModel: String, byField: String, byValues: List<String>): String {
        if (byValues.size > 100) logger.warn("attention : ne peut pas gérer plus de ~100 filtres dans l'url")

        val idsFilter = byValues.joinToString("") { "&filters[$byField][\$in]=$it" }
        val uri = "${cmsModel}?populate=*$idsFilter"

        val request = getClientRequest(uri).GET().build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
    }

    fun getAllBetweenDates(cmsModel: String, beginField: String, endField: String, dateBetween: LocalDateTime): String {
        val formattedBetweenDate = dateBetween.format(DateTimeFormatter.ISO_DATE_TIME)

        val filter = "&filters[$beginField][\$lt]=$formattedBetweenDate&filters[$endField][\$gt]=$formattedBetweenDate"
        val uri = "${cmsModel}?pagination[pageSize]=100&populate=*$filter"

        val request = getClientRequest(uri).GET().build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
    }

    fun getAllBeforeDate(cmsModel: String, dateField: String, dateAfter: LocalDateTime): String {
        val formattedDate = dateAfter.format(DateTimeFormatter.ISO_DATE_TIME)

        val filter = "&filters[$dateField][\$lt]=$formattedDate"
        val uri = "${cmsModel}?pagination[pageSize]=100&populate=*$filter"

        val request = getClientRequest(uri).GET().build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
    }

    fun getAllSortedBy(cmsModel: String, sortField: String, sortByDesc: Boolean = true): String {
        val sortDirection = if (sortByDesc) "desc" else "asc"
        val uri = "${cmsModel}?populate=*&sort[0]=$sortField:$sortDirection&pagination[pageSize]=100"

        val request = getClientRequest(uri).GET().build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
    }

    fun getAll(cmsModel: String): String {
        val uri = "${cmsModel}?populate=*&pagination[pageSize]=100"

        val request = getClientRequest(uri).GET().build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
    }

    fun count(cmsModel: String): String {
        val uri = "${cmsModel}?pagination[pageSize]=1&populate=*"

        val request = getClientRequest(uri).GET().build()
        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return httpResponse.body()
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

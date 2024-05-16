package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import java.time.LocalDate


@Component
@Primary
@Suppress("unused")
@Qualifier("cmsResponseQagRepository")
class ResponseQagRepositoryStrapiImpl(
    private val mapper: ResponseQagMapper,
    private val objectMapper: ObjectMapper,
) : ResponseQagRepository {

    override fun getResponsesQag(qagIds: List<String>): List<ResponseQag> {
        val apiKey =
            "Bearer 48b70866e9120e3afb8b35466b42377ea8360245b456044ea2c733f7d51943502ea142561605f7a25bc839aaa2d8b5d07575dc1f0f64e0eb74a9be5213e98b1b4463e4116a55f2bf6da1f3f92ddde69316552ebf3f8788eef2bca27971c6a4d6c325741c493ca86e53fd860623075b7603bb22f5756d9d9722cd15978881ac55"

        val client: HttpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(URI("https://cms.agora.incubateur.net/api/reponse-du-gouvernements?populate=*"))
            .GET()
            .setHeader("Authorization", apiKey)
            .build()
        val response: HttpResponse<String> = client.send(request, BodyHandlers.ofString())
        val xx = objectMapper
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .readValue(response.body(), StrapiArrayResponse::class.java)

        return toResponseQag(xx)
    }

    data class StrapiArrayResponse(
        @JsonProperty("data")
        val data: List<StrapiModelAttributes>,
    )

    data class StrapiModelAttributes(
        @JsonProperty(value = "attributes")
        val attributes: StrapiResponseQag
    )

    data class StrapiResponseQag(
        @JsonProperty(value = "auteur")
        val auteur: String,
        @JsonProperty(value = "auteurPortraitUrl")
        val auteurPortraitUrl: String,
        @JsonProperty(value = "reponseDate")
        val reponseDate: LocalDate,
        @JsonProperty(value = "feedbackQuestion")
        val feedbackQuestion: String,
        @JsonProperty(value = "questionId")
        val questionId: String,
        @JsonProperty("reponseType")
        val reponseType: List<StrapiResponseQagType>
    )

    data class StrapiResponseQagType(
        @JsonProperty("label")
        val label: String,
        @JsonProperty("text")
        val text: String,
    )

    fun toResponseQag(responseBody: StrapiArrayResponse): List<ResponseQag> {
        return responseBody.data.map {
            val response = it.attributes
            ResponseQagText(
                author = response.auteur,
                authorPortraitUrl = response.auteurPortraitUrl,
                responseDate = response.reponseDate.toDate(),
                feedbackQuestion = response.feedbackQuestion,
                qagId = response.questionId,
                responseText = response.reponseType.first().text,
                responseLabel = response.reponseType.first().label,
            )
        }
    }

    override fun getResponseQag(qagId: String): ResponseQag? {
        TODO()
    }

    override fun getResponsesQagCount(): Int {
        TODO()
    }

    override fun getResponsesQag(offset: Int): List<ResponseQag> {
        TODO()
    }
}

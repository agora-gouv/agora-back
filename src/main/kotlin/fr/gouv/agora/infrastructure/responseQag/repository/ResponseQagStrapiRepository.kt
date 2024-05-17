package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.LocalDate
import java.util.UUID

@Repository
class ResponseQagStrapiRepository(
    private val objectMapper: ObjectMapper,
) {
    fun getResponsesQag(@Param("qagIds") qagIds: List<UUID>): List<ResponseQagDTO> {
        TODO()
    }

    fun getResponseQag(@Param("qagId") qagId: UUID): ResponseQagDTO? {
        TODO()
    }

    fun getResponsesQagCount(): Int {
        TODO()
    }

    fun getResponsesQag(@Param("offset") offset: Int): List<ResponseQag> {
        TODO()
    }
}

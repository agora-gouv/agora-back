package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
class ResponseQagStrapiRepository(
    private val objectMapper: ObjectMapper,
    private val responseQagMapper: ResponseQagMapper,
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    fun getResponsesQag(@Param("qagIds") qagIds: List<UUID>): List<ResponseQag> {
        val allResponses = cmsStrapiHttpClient
            .getByIds("reponse-du-gouvernements", "questionId", qagIds.map { it.toString() })

        val responseQagFromStrapi = objectMapper
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .readValue(allResponses, StrapiResponseDTO::class.java)

        return responseQagMapper.toDomain(responseQagFromStrapi)
    }
}

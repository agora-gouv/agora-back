package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQagDTO
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ResponseQagStrapiRepository(
    private val objectMapper: ObjectMapper,
    private val responseQagMapper: ResponseQagMapper,
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    fun getResponsesQag(@Param("qagIds") qagIds: List<UUID>): List<ResponseQag> {
        val allResponsesQag = cmsStrapiHttpClient
            .getByIds("reponse-du-gouvernements", "questionId", qagIds.map { it.toString() })

        val responseQagFromStrapi = objectMapper
            .readValue(allResponsesQag, StrapiResponseQagDTO::class.java)

        return responseQagMapper.toDomain(responseQagFromStrapi)
    }
}

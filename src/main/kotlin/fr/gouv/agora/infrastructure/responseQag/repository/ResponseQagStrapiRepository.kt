package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQag
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ResponseQagStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    fun getResponsesQag(qagIds: List<UUID>): StrapiDTO<StrapiResponseQag> {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")
            .filterBy("questionId", qagIds.map { it.toString() })

        return cmsStrapiHttpClient.request(uriBuilder)
    }

    fun getResponsesQag(): StrapiDTO<StrapiResponseQag> {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")
            .sortBy("reponseDate", "desc")

        return cmsStrapiHttpClient.request(uriBuilder)
    }

    fun getResponsesCount(): Int {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")

        return cmsStrapiHttpClient.request<StrapiResponseQag>(uriBuilder).meta.pagination.total
    }
}

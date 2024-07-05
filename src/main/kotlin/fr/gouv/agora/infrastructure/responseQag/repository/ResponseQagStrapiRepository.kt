package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQag
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ResponseQagStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<StrapiResponseQag>>() {}

    fun getResponsesQag(qagIds: List<UUID>): StrapiDTO<StrapiResponseQag> {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")
            .filterBy("questionId", qagIds.map { it.toString() })

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getResponsesQag(): StrapiDTO<StrapiResponseQag> {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")
            .sortBy("reponseDate", "desc")

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getResponsesCount(): Int {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")

        return cmsStrapiHttpClient.request<StrapiResponseQag>(uriBuilder, ref).meta.pagination.total
    }
}

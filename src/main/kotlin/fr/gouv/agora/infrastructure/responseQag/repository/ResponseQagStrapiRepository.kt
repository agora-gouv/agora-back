package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.core.type.TypeReference
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
    companion object {
        private val POPULATE = listOf(
            "[auteurPortrait][fields][0]=url",
            "[auteurPortrait][fields][1]=formats",
            "[reponseType][populate][on][reponse.reponse-video][populate][video]=*",
            "[reponseType][populate][on][reponse.reponsetextuelle]=*",
        ).joinToString("&populate") { it }
    }

    val ref = object : TypeReference<StrapiDTO<StrapiResponseQag>>() {}

    fun getResponsesQag(qagIds: List<UUID>): StrapiDTO<StrapiResponseQag> {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")
            .filterIn("questionId", qagIds.map { it.toString() })
            .populate(POPULATE)

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getResponsesQag(): StrapiDTO<StrapiResponseQag> {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")
            .sortBy("reponseDate", "desc")
            .populate(POPULATE)

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getResponsesCount(): Int {
        val uriBuilder = StrapiRequestBuilder("reponse-du-gouvernements")

        return cmsStrapiHttpClient.request<StrapiResponseQag>(uriBuilder, ref).meta.pagination.total
    }
}

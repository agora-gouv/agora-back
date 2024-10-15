package fr.gouv.agora.infrastructure.content.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.springframework.stereotype.Repository

@Repository
class PagePoserMaQuestionStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<PagePoserMaQuestionStrapiDTO>>() {}

    fun getFirst(): StrapiAttributes<PagePoserMaQuestionStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("page-poser-ma-question")

        return cmsStrapiHttpClient.request<PagePoserMaQuestionStrapiDTO>(uriBuilder, ref).data.first()
    }
}

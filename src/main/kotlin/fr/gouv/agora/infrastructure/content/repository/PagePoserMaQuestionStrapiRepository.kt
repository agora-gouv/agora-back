package fr.gouv.agora.infrastructure.content.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.common.StrapiSingleTypeDTO
import org.springframework.stereotype.Repository

@Repository
class PagePoserMaQuestionStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiSingleTypeDTO<PagePoserMaQuestionStrapiDTO>>() {}

    fun getFirst(): StrapiAttributes<PagePoserMaQuestionStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("page-poser-ma-question")

        // TODO ne pas utiliser le requestSingleType mais avoir un type générique ?
        return cmsStrapiHttpClient.requestSingleType<PagePoserMaQuestionStrapiDTO>(uriBuilder, ref).data
    }
}

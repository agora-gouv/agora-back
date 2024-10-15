package fr.gouv.agora.infrastructure.content.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.common.StrapiSingleTypeDTO
import org.springframework.stereotype.Repository

@Repository
class PageReponseAuxQuestionsAuGouvernementStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiSingleTypeDTO<PageReponseAuxQuestionsAuGouvernementStrapiDTO>>() {}

    fun getFirst(): StrapiAttributes<PageReponseAuxQuestionsAuGouvernementStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("page-reponse-aux-questions-au-gouvernement")

        return cmsStrapiHttpClient.requestSingleType<PageReponseAuxQuestionsAuGouvernementStrapiDTO>(uriBuilder, ref).data
    }
}

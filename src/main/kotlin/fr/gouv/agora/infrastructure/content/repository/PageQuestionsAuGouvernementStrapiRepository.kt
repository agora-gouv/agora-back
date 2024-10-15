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
class PageQuestionsAuGouvernementStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiSingleTypeDTO<PageQuestionsAuGouvernementStrapiDTO>>() {}

    fun getFirst(): StrapiAttributes<PageQuestionsAuGouvernementStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("page-questions-au-gouvernement")

        return cmsStrapiHttpClient.requestSingleType<PageQuestionsAuGouvernementStrapiDTO>(uriBuilder, ref).data
    }
}

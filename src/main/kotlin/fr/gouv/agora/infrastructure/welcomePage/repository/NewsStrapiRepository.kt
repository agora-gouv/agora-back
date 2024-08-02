package fr.gouv.agora.infrastructure.welcomePage.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.springframework.stereotype.Repository

@Repository
class NewsStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<NewsStrapiDTO>>() {}

    fun getNews(): List<StrapiAttributes<NewsStrapiDTO>> {
        val uriBuilder = StrapiRequestBuilder("welcome-page-news")
            .sortBy("date_de_debut", "desc")

        return cmsStrapiHttpClient.request<NewsStrapiDTO>(uriBuilder, ref).data
    }
}

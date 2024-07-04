package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.thematique.dto.StrapiThematiqueDTO
import org.springframework.stereotype.Repository

@Repository
class ThematiqueStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    fun getThematiques(): StrapiDTO<StrapiThematiqueDTO> {
        val uriBuilder = StrapiRequestBuilder("thematiques")

        return cmsStrapiHttpClient.request(uriBuilder)
    }
}

package fr.gouv.agora.infrastructure.ficheInventaire

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.springframework.stereotype.Repository

@Repository
class FicheInventaireStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<FicheInventaireStrapiDTO>>() {}

    fun getFichesInventaire(): StrapiDTO<FicheInventaireStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("fiche-inventaires")

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }
}

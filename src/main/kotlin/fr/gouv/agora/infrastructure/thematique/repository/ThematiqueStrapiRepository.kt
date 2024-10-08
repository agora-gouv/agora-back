package fr.gouv.agora.infrastructure.thematique.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import org.springframework.stereotype.Repository

@Repository
class ThematiqueStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<ThematiqueStrapiDTO>>() {}

    fun getThematiques(): StrapiDTO<ThematiqueStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("thematiques")

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }
}

package fr.gouv.agora.infrastructure.concertations

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.springframework.stereotype.Repository

@Repository
class ConcertationStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<ConcertationStrapiDTO>>() {}

    fun getAll(): StrapiDTO<ConcertationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("concertations")

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }
}

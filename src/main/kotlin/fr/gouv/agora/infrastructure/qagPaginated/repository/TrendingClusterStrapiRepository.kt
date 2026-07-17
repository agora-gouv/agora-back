package fr.gouv.agora.infrastructure.qagPaginated.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.springframework.stereotype.Repository

@Repository
class TrendingClusterStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    private val ref = object : TypeReference<StrapiDTO<TrendingClusterStrapiDTO>>() {}

    fun getClusters(): StrapiDTO<TrendingClusterStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("cluster-semaine-libres")
        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }
}

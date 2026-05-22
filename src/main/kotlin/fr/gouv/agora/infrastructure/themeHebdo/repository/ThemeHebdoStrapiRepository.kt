
package fr.gouv.agora.infrastructure.themeHebdo.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.springframework.stereotype.Repository

@Repository
class ThemeHebdoStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<ThemeHebdoStrapiDTO>>() {}

    fun getThemeHebdo(): StrapiDTO<ThemeHebdoStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("theme-hebdos")

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }
}

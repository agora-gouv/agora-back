package fr.gouv.agora.infrastructure.headerQag.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.headerQag.dto.HeaderQagStrapiDTO
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class HeaderQagStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<HeaderQagStrapiDTO>>() {}

    fun getLastHeader(type: String, date: LocalDateTime): StrapiAttributes<HeaderQagStrapiDTO>? {
        val uriBuilder = StrapiRequestBuilder("qa-g-headers-onglet")
            .withDateBefore(date, "datetime_de_debut")
            .filterBy("type", listOf(type))
            .sortBy("datetime_de_debut", "desc")

        return cmsStrapiHttpClient.request<HeaderQagStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }
}

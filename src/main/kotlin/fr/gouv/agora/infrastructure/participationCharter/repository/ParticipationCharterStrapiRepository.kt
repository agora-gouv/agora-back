package fr.gouv.agora.infrastructure.participationCharter.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.participationCharter.dto.ParticipationCharterStrapiDTO
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ParticipationCharterStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<ParticipationCharterStrapiDTO>>() {}

    fun getLastParticipationCharter(now: LocalDateTime): StrapiAttributes<ParticipationCharterStrapiDTO>? {
        val uriBuilder = StrapiRequestBuilder("charte-participations")
            .withDateBefore(now, "datetime_debut")
            .sortBy("datetime_debut", "desc")

        return cmsStrapiHttpClient.request<ParticipationCharterStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }
}

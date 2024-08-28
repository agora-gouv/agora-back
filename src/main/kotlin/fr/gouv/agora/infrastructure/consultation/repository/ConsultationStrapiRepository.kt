package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.LocalDateTime

@Repository
class ConsultationStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
    private val clock: Clock,
) {
    val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}

    fun getConsultationsOngoingWithUnpublished(date: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_debut")
            .withDateAfter(date, "datetime_de_fin")
            .withUnpublished()

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationsFinishedWithUnpublished(date: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_fin")
            .withUnpublished()

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationsByIds(consultationIds: List<String>): StrapiDTO<ConsultationStrapiDTO> {
        val strapiConsultationsId = consultationIds.mapNotNull { it.toIntOrNull() }
        if (strapiConsultationsId.isEmpty()) return StrapiDTO.ofEmpty()

        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(strapiConsultationsId)

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationBySlugWithUnpublished(slug: String): StrapiAttributes<ConsultationStrapiDTO>? {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .filterBy("slug", listOf(slug))
            .withUnpublished()

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }

    fun getConsultationById(consultationId: String): StrapiAttributes<ConsultationStrapiDTO>? {
        val strapiConsultationId = consultationId.toIntOrNull() ?: return null
        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(listOf(strapiConsultationId))

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }

    fun getConsultationByIdWithUnpublished(consultationId: String): StrapiAttributes<ConsultationStrapiDTO>? {
        val strapiConsultationId = consultationId.toIntOrNull() ?: return null
        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(listOf(strapiConsultationId))
            .withUnpublished()

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }

    fun getConsultationsEnded14DaysAgo(today: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(today.minusDays(14), "datetime_de_fin")

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun isConsultationExists(consultationId: String): Boolean {
        val strapiConsultationId = consultationId.toIntOrNull() ?: return false
        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(listOf(strapiConsultationId))

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref)
            .meta.pagination.total == 1
    }

    fun getLastUpdateLabelFromConsultation(consultationId: String): String? {
        val consultationIntId = consultationId.toIntOrNull() ?: return null
        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(listOf(consultationIntId))

        val now = LocalDateTime.now(clock)
        val consultation = cmsStrapiHttpClient
            .request<ConsultationStrapiDTO>(uriBuilder, ref)

        return consultation.data.firstOrNull()?.attributes?.getFlammeLabel(now)
    }
}

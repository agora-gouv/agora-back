package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ConsultationStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    companion object {
        private val SECTION_TYPES = listOf(
            "consultation-section.section-titre",
            "consultation-section.section-texte-riche",
            "consultation-section.section-citation",
            "consultation-section.section-image",
            "consultation-section.section-video",
            "consultation-section.section-chiffre",
            "consultation-section.section-accordeon",
        )

        private val CONTENU_WITH_SECTIONS = listOf(
            "consultation_avant_reponse",
            "consultation_apres_reponse_ou_terminee",
            "consultation_contenu_autres",
            "consultation_contenu_analyse_des_reponse",
            "contenu_reponse_du_commanditaires",
        )

        private val COMMON_MEDIA_POPULATE = listOf(
            "[image_de_couverture][fields][0]=url",
            "[image_de_couverture][fields][1]=formats",
            "[image_page_de_contenu][fields][0]=url",
            "[image_page_de_contenu][fields][1]=formats",
        )

        // Pour les requêtes de liste : sections non incluses
        private val LIST_POPULATE = (listOf(
            "[thematique]=*",
            "[questions][populate]=*",
            "[consultation_contenu_a_venir]=*",
        ) + CONTENU_WITH_SECTIONS
            .filter { it != "consultation_contenu_analyse_des_reponse" }
            .map { "[${it}][populate]=*" }
            + listOf("[consultation_contenu_analyse_des_reponse][populate][pdf_analyse][fields][0]=url")
            + COMMON_MEDIA_POPULATE
        ).joinToString("&populate") { it }

        // Pour les requêtes de détail : sections via Fragment API + questions
        private val DETAIL_POPULATE = (listOf(
            "[thematique]=*",
            "[questions][populate]=*",
            "[consultation_contenu_a_venir]=*",
        ) + CONTENU_WITH_SECTIONS.flatMap { contenu ->
            SECTION_TYPES.map { sectionType ->
                "[${contenu}][populate][sections][on][${sectionType}][populate]=*"
            }
        } + listOf("[consultation_contenu_analyse_des_reponse][populate][pdf_analyse][fields][0]=url")
            + COMMON_MEDIA_POPULATE
        ).joinToString("&populate") { it }
    }

    val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}

    fun getConsultationsOngoing(
        date: LocalDateTime,
        territories: List<Territoire>
    ): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_debut")
            .withDateAfter(date, "datetime_de_fin")
            .populate(LIST_POPULATE)

        if (territories.isNotEmpty())
            uriBuilder.filterIn("territoire", territories.map { it.value })

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationsOngoingWithUnpublished(
        date: LocalDateTime,
        territories: List<Territoire>
    ): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_debut")
            .withDateAfter(date, "datetime_de_fin")
            .withUnpublished()
            .populate(LIST_POPULATE)

        if (territories.isNotEmpty())
            uriBuilder.filterIn("territoire", territories.map { it.value })

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationsFinished(
        date: LocalDateTime,
        territories: List<Territoire>
    ): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_fin")
            .populate(LIST_POPULATE)

        if (territories.isNotEmpty())
            uriBuilder.filterIn("territoire", territories.map { it.value })

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationsFinishedWithUnpublished(
        date: LocalDateTime,
        territories: List<Territoire>
    ): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_fin")
            .withUnpublished()
            .populate(LIST_POPULATE)

        if (territories.isNotEmpty())
            uriBuilder.filterIn("territoire", territories.map { it.value })

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationsFinishedByTerritories(
        date: LocalDateTime,
        territories: List<Territoire>
    ): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_fin")
            .populate(LIST_POPULATE)

        if (territories.isNotEmpty())
            uriBuilder.filterIn("territoire", territories.map { it.value })

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationsByIds(consultationIds: List<String>): StrapiDTO<ConsultationStrapiDTO> {
        if (consultationIds.isEmpty()) return StrapiDTO.ofEmpty()

        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(consultationIds)
            .populate(LIST_POPULATE)

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getConsultationBySlug(slug: String): ConsultationStrapiDTO? {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .filterIn("slug", listOf(slug))
            .populate(DETAIL_POPULATE)

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }

    fun getConsultationBySlugWithUnpublished(slug: String): ConsultationStrapiDTO? {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .filterIn("slug", listOf(slug))
            .withUnpublished()
            .populate(DETAIL_POPULATE)

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }

    fun getConsultationById(consultationId: String): ConsultationStrapiDTO? {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(listOf(consultationId))
            .withUnpublished()
            .populate(DETAIL_POPULATE)

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }

    fun getConsultationByIdWithUnpublished(consultationId: String): ConsultationStrapiDTO? {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(listOf(consultationId))
            .withUnpublished()
            .populate(DETAIL_POPULATE)

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }

    fun getConsultationsEnded14DaysAgo(today: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(today.minusDays(14), "datetime_de_fin")
            .populate(LIST_POPULATE)

        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun isConsultationExists(consultationId: String): Boolean {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .getByIds(listOf(consultationId))
            .withUnpublished()

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref)
            .meta.pagination.total == 1
    }

    fun countFinishedConsultations(date: LocalDateTime): Int {
        val uriBuilder = StrapiRequestBuilder("consultations")
            .withDateBefore(date, "datetime_de_fin")

        return cmsStrapiHttpClient.request<ConsultationStrapiDTO>(uriBuilder, ref)
            .meta.pagination.total
    }
}

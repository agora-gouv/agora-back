package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ConsultationStrapiRepository(
    private val objectMapper: ObjectMapper,
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    private val logger = LoggerFactory.getLogger(ConsultationStrapiRepository::class.java)

    fun getConsultationsOngoing(date: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        return try {
            val consultations = cmsStrapiHttpClient
                .getAllBetweenDates("consultations", "datetime_de_debut", "datetime_de_fin", date)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations ongoing Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    fun getConsultationsFinished(date: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        return try {
            val consultations = cmsStrapiHttpClient
                .getAllBeforeDate("consultations", "datetime_de_fin", date)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations ongoing Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    fun getConsultationsByIds(consultationIds: List<Int>): StrapiDTO<ConsultationStrapiDTO> {
        return try {
            val consultations = cmsStrapiHttpClient
                .getByIds("consultations", *consultationIds.toIntArray())

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations par Ids Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    fun getConsultationById(consultationId: String): StrapiDTO<ConsultationStrapiDTO> {
        return try {
            val consultationIdInt = consultationId.toIntOrNull() ?: return StrapiDTO.ofEmpty()
            val consultations = cmsStrapiHttpClient
                .getByIds("consultations", consultationIdInt)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération d'une consultation par Id Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    fun getConsultationsEnded14DaysAgo(today: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        return try {
            val consultations = cmsStrapiHttpClient
                .getAllBeforeDate("consultations", "datetime_de_fin", today.minusDays(14))

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations par Ids Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    fun isConsultationExists(consultationId: String): Boolean {
        return try {
            val consultationIdInt = consultationId.toIntOrNull() ?: return false
            val consultations = cmsStrapiHttpClient.getByIds("consultations", consultationIdInt)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref).meta.pagination.total == 1
        } catch (e: Exception) {
            logger.error("Erreur lors de la vérification de l'existance d'une consultation Strapi : ", e)

            false
        }
    }
}

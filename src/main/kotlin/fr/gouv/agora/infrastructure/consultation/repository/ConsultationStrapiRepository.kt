package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ConsultationStrapiRepository(
    private val objectMapper: ObjectMapper,
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    private val logger = LoggerFactory.getLogger(ConsultationStrapiRepository::class.java)

    fun getConsultationsWithEndDateAfter(date: LocalDate): StrapiDTO<ConsultationStrapiDTO> {
        return try {
            val consultations = cmsStrapiHttpClient
                .getAllAfterOrEqual("consultations", "date_de_fin", date)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }
}

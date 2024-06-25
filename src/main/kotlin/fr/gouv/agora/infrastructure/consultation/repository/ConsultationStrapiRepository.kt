package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate
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
                .getAllAfterOrEqual("consultations", "date_de_fin", date)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    fun getConsultationsFinished(date: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        return try {
            val consultations = cmsStrapiHttpClient
                .getAllBefore("consultations", "date_de_fin", date)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            return objectMapper.readValue(consultations, ref)
//            val consultationss = objectMapper.readValue(consultations, ref)

//            consultationss.data.map {
//                it.attributes.consultationContenuAutres.map {
//                    it.attributes.datePublication
//                }
//            }
//            return consultationss
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    // consultations avec une endDate avant aujourd'hui
    //        // AND une date d'update après ou égale à aujourd'hui
    //        // AND l'update la plus récente
}

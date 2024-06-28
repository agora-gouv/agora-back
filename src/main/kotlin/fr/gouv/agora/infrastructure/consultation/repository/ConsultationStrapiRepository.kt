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
                .getAllBetweenDates("consultations", "date_de_debut", "date_de_fin", date)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations ongoing Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }

    fun getConsultationsFinished(date: LocalDateTime): StrapiDTO<ConsultationStrapiDTO> {
        // TODO : idée : si on utilise le builder, on pourrait l'instancier ici, le filer au Client
        // TODO : et le client pourrait même nous retourner des objets DTOs
        // TODO : ce qui nous permettrait de faire un filtre ici aussi

        return try {
            val consultations = cmsStrapiHttpClient
                .getAllBeforeDate("consultations", "date_de_fin", date)

            val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
            objectMapper.readValue(consultations, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des consultations ongoing Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }
}

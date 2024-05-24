package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQagDTO
import org.slf4j.LoggerFactory
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ResponseQagStrapiRepository(
    private val objectMapper: ObjectMapper,
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    private val logger = LoggerFactory.getLogger(ResponseQagRepositoryImpl::class.java)

    fun getResponsesQag(@Param("qagIds") qagIds: List<UUID>): StrapiResponseQagDTO {
        logger.debug("Récupération des réponses de QaG via Strapi pour les ids {}", qagIds)

        return try {
            val allResponsesQag = cmsStrapiHttpClient
                .getByIds("reponse-du-gouvernements", "questionId", qagIds.map { it.toString() })

            objectMapper.readValue(allResponsesQag, StrapiResponseQagDTO::class.java)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des réponses QaG Strapi via ids : ", e)

            StrapiResponseQagDTO.ofEmpty()
        }
    }

    fun getResponsesQag(): StrapiResponseQagDTO {
        logger.debug("Récupération des réponses de QaG via Strapi")

        return try {
            val allResponsesQag = cmsStrapiHttpClient
                .getAllSortedBy("reponse-du-gouvernements", "response_date", true)

            objectMapper.readValue(allResponsesQag, StrapiResponseQagDTO::class.java)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des réponses QaG Strapi : ", e)

            StrapiResponseQagDTO.ofEmpty()
        }
    }

    fun getResponsesCount(): Int {
        logger.debug("Récupération du nombre de réponses de QaG via Strapi")

        return try {
            val allResponsesQag = cmsStrapiHttpClient.count("reponse-du-gouvernements")

            val responseQagDTO = objectMapper.readValue(allResponsesQag, StrapiResponseQagDTO::class.java)

            return responseQagDTO.meta.pagination.total
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération du nombre de réponses QaG Strapi via ids : ", e)

            0
        }
    }
}

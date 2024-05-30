package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQag
import fr.gouv.agora.infrastructure.common.StrapiDTO
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

    fun getResponsesQag(@Param("qagIds") qagIds: List<UUID>): StrapiDTO<StrapiResponseQag> {
        return try {
            val allResponsesQag = cmsStrapiHttpClient
                .getByIds("reponse-du-gouvernements", "questionId", qagIds.map { it.toString() })

            val ref: TypeReference<*> = object : TypeReference<StrapiDTO<StrapiResponseQag>>() {}
            objectMapper.readValue(allResponsesQag, ref) as StrapiDTO<StrapiResponseQag>
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des réponses QaG Strapi via ids : ", e)
            StrapiDTO.ofEmpty()
        }
    }

    fun getResponsesQag(): StrapiDTO<StrapiResponseQag> {
        return try {
            val allResponsesQag = cmsStrapiHttpClient
                .getAllSortedBy("reponse-du-gouvernements", "reponseDate", true)

            val ref: TypeReference<*> = object : TypeReference<StrapiDTO<StrapiResponseQag>>() {}
            objectMapper.readValue(allResponsesQag, ref) as StrapiDTO<StrapiResponseQag>
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des réponses QaG Strapi : ", e)
            StrapiDTO.ofEmpty()
        }
    }

    fun getResponsesCount(): Int {
        return try {
            val allResponsesQag = cmsStrapiHttpClient.count("reponse-du-gouvernements")

            val ref: TypeReference<*> = object : TypeReference<StrapiDTO<StrapiResponseQag>>() {}
            val responseQagDTO = objectMapper.readValue(allResponsesQag, ref) as StrapiDTO<StrapiResponseQag>

            return responseQagDTO.meta.pagination.total
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération du nombre de réponses QaG Strapi via ids : ", e)

            0
        }
    }
}

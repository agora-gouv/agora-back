package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.thematique.dto.StrapiThematiqueDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ThematiqueStrapiRepository(
    private val objectMapper: ObjectMapper,
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    private val logger = LoggerFactory.getLogger(ThematiqueStrapiRepository::class.java)

    fun getThematiques(): StrapiDTO<StrapiThematiqueDTO> {
        return try {
            val allThematiques = cmsStrapiHttpClient.getAll("thematiques")

            val ref = object : TypeReference<StrapiDTO<StrapiThematiqueDTO>>() {}
            objectMapper.readValue(allThematiques, ref)
        } catch (e: Exception) {
            logger.error("Erreur lors de la récupération des thématiques Strapi : ", e)

            StrapiDTO.ofEmpty()
        }
    }
}

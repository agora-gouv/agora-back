package fr.gouv.agora.infrastructure.consultationUpdates.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import org.springframework.stereotype.Repository

@Repository
class ConsultationUpdateStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}

//    fun getConsultationById(consultationId: String): StrapiDTO<ConsultationStrapiDTO> {
//        val strapiConsultationId = consultationId.toIntOrNull() ?: return StrapiDTO.ofEmpty()
//        val uriBuilder = StrapiRequestBuilder("consultations")
//            .getByIds(listOf(strapiConsultationId))
//
//        return cmsStrapiHttpClient.request(uriBuilder, ref)
//    }
}

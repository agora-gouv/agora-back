package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagAdditionalInfo
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.ResponseQagVideo
import fr.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.springframework.stereotype.Component

@Component
class ResponseQagMapper {

    fun toDomain(dto: ResponseQagDTO): ResponseQag? {
        return if (!dto.videoUrl.isNullOrEmpty() && !dto.authorDescription.isNullOrEmpty() && dto.videoWidth != null && dto.videoHeight != null && !dto.transcription.isNullOrEmpty())
            ResponseQagVideo(
                author = dto.author,
                authorPortraitUrl = dto.authorPortraitUrl,
                authorDescription = dto.authorDescription,
                responseDate = dto.responseDate,
                videoUrl = dto.videoUrl,
                videoWidth = dto.videoWidth,
                videoHeight = dto.videoHeight,
                transcription = dto.transcription,
                feedbackQuestion = dto.feedbackQuestion,
                qagId = dto.qagId.toString(),
                additionalInfo = if (dto.additionalInfoTitle != null && dto.additionalInfoDescription != null) ResponseQagAdditionalInfo(
                    additionalInfoTitle = dto.additionalInfoTitle,
                    additionalInfoDescription = dto.additionalInfoDescription
                ) else null,
            )
        else if (!dto.responseText.isNullOrEmpty())
            ResponseQagText(
                author = dto.author,
                authorPortraitUrl = dto.authorPortraitUrl,
                responseDate = dto.responseDate,
                responseLabel = dto.responseLabel,
                responseText = dto.responseText,
                feedbackQuestion = dto.feedbackQuestion,
                qagId = dto.qagId.toString(),
            )
        else null
    }

    fun toDomain(responseBody: StrapiResponseDTO): List<ResponseQag> {
        return responseBody.data.map {
            val response = it.attributes
            ResponseQagText(
                author = response.auteur,
                authorPortraitUrl = response.auteurPortraitUrl,
                responseDate = response.reponseDate.toDate(),
                feedbackQuestion = response.feedbackQuestion,
                qagId = response.questionId,
                responseText = response.reponseType.first().text,
                responseLabel = response.reponseType.first().label,
            )
        }
    }
}

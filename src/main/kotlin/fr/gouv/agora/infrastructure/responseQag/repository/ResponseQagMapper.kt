package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.ResponseQagVideo
import fr.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import org.springframework.stereotype.Component

@Component
class ResponseQagMapper {

    fun toDomain(dto: ResponseQagDTO): ResponseQag? {
        return if (!dto.videoUrl.isNullOrEmpty())
            ResponseQagVideo(
                author = dto.author,
                authorPortraitUrl = dto.authorPortraitUrl,
                authorDescription = dto.authorDescription?: throw IllegalStateException("authorDescription must be non-null for video response"),
                responseDate = dto.responseDate,
                videoUrl = dto.videoUrl,
                videoWidth = dto.videoWidth?: throw IllegalStateException("videoWidth must be non-null for video response"),
                videoHeight = dto.videoHeight?: throw IllegalStateException("videoHeight must be non-null for video response"),
                transcription = dto.transcription?: throw IllegalStateException("transcription must be non-null for video response"),
                feedbackQuestion = dto.feedbackQuestion,
                qagId = dto.qagId.toString(),
            )
        else if (!dto.responseText.isNullOrEmpty())
            ResponseQagText(
            responseLabel = dto.responseLabel,
            responseText = dto.responseText,
            feedbackQuestion = dto.feedbackQuestion,
            qagId = dto.qagId.toString(),
        )
        else null
    }
}
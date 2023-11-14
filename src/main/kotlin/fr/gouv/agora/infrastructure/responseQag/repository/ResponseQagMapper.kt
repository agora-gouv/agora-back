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
            dto.authorDescription?.let { authorDescription ->
                dto.videoWidth?.let { videoWidth ->
                    dto.videoHeight?.let { videoHeight ->
                        dto.transcription?.let { transcription ->
                            ResponseQagVideo(
                                author = dto.author,
                                authorPortraitUrl = dto.authorPortraitUrl,
                                authorDescription = authorDescription,
                                responseDate = dto.responseDate,
                                videoUrl = dto.videoUrl,
                                videoWidth = videoWidth,
                                videoHeight = videoHeight,
                                transcription = transcription,
                                feedbackQuestion = dto.feedbackQuestion,
                                qagId = dto.qagId.toString(),
                            )
                        }
                    }
                }
            }
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
}
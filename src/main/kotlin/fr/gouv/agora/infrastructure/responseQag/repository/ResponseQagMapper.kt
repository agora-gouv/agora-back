package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import org.springframework.stereotype.Component

@Component
class ResponseQagMapper {

    fun toDomain(dto: ResponseQagDTO): ResponseQag {
        return ResponseQag(
            id = dto.id.toString(),
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
        )
    }

}
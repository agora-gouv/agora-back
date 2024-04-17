package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FeedbackQagMapper {

    companion object {
        private const val IS_HELPFUL_TRUE_VALUE = 1
        private const val IS_HELPFUL_FALSE_VALUE = 0
    }

    fun toDto(domain: FeedbackQagInserting): FeedbackQagDTO? {
        return toDto(domain, UUID.randomUUID())
    }

    fun toDto(domain: FeedbackQagInserting, feedbackId: UUID): FeedbackQagDTO? {
        return try {
            FeedbackQagDTO(
                id = feedbackId,
                qagId = UUID.fromString(domain.qagId),
                userId = UUID.fromString(domain.userId),
                isHelpful = if (domain.isHelpful) IS_HELPFUL_TRUE_VALUE else IS_HELPFUL_FALSE_VALUE,
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun toDomain(dto: FeedbackQagDTO): FeedbackQag {
        return FeedbackQag(
            qagId = dto.qagId.toString(),
            userId = dto.userId.toString(),
            isHelpful = dto.isHelpful == IS_HELPFUL_TRUE_VALUE,
        )
    }
}
package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class FeedbackQagMapper {

    companion object {
        private const val IS_HELPFUL_TRUE_VALUE = 1
        private const val IS_HELPFUL_FALSE_VALUE = 0
    }

    fun toDto(domain: FeedbackQagInserting): FeedbackQagDTO? {
        return try {
            FeedbackQagDTO(
                id = UUID.randomUUID(),
                userId = UUID.fromString(domain.userId),
                qagId = UUID.fromString(domain.qagId),
                isHelpful = if (domain.isHelpful) IS_HELPFUL_TRUE_VALUE
                else IS_HELPFUL_FALSE_VALUE,
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
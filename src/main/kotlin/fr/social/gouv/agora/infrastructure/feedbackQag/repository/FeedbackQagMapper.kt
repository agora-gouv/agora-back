package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class FeedbackQagMapper {
    fun toDto(domain: FeedbackQag): FeedbackQagDTO? {
        return try {
            FeedbackQagDTO(
                id = UUID.randomUUID(),
                userId = domain.userId,
                qagId = UUID.fromString(domain.qagId),
                isHelpful = domain.isHelpful,
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
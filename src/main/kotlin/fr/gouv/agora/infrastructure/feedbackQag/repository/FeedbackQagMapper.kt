package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class FeedbackQagMapper {

    companion object {
        private const val BOOLEAN_INT_TRUE_VALUE = 1
        private const val BOOLEAN_INT_FALSE_VALUE = 0
    }

    fun modifyIsHelpful(dto: FeedbackQagDTO, isHelpful: Boolean): FeedbackQagDTO {
        return dto.copy(
            isHelpful = toInt(isHelpful),
            updatedDate = Date(),
        )
    }

    fun toDto(domain: FeedbackQagInserting): FeedbackQagDTO? {
        return try {
            val createdDate = Date()
            FeedbackQagDTO(
                id = UUID.randomUUID(),
                qagId = UUID.fromString(domain.qagId),
                userId = UUID.fromString(domain.userId),
                isHelpful = toInt(domain.isHelpful),
                createdDate = createdDate,
                updatedDate = createdDate,
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun toDomain(dto: FeedbackQagDTO): FeedbackQag {
        return FeedbackQag(
            qagId = dto.qagId.toString(),
            userId = dto.userId.toString(),
            isHelpful = toBoolean(dto.isHelpful),
        )
    }

    private fun toBoolean(boolIntValue: Int): Boolean {
        return boolIntValue == BOOLEAN_INT_TRUE_VALUE
    }

    private fun toInt(isHelpful: Boolean): Int {
        return if (isHelpful) BOOLEAN_INT_TRUE_VALUE else BOOLEAN_INT_FALSE_VALUE
    }
}
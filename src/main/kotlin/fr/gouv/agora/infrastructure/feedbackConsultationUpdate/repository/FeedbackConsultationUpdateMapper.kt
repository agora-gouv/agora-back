package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateDTO
import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateStatsDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import org.springframework.stereotype.Component
import java.util.*
import kotlin.math.roundToInt

@Component
class FeedbackConsultationUpdateMapper {

    companion object {
        private const val IS_POSITIVE_TRUE_VALUE = 1
        private const val IS_POSITIVE_FALSE_VALUE = 0
    }

    fun toDto(feedbackInserting: FeedbackConsultationUpdateInserting): FeedbackConsultationUpdateDTO? {
        val userId = feedbackInserting.userId.toUuidOrNull() ?: return null
        val createdDate = Date()

        return FeedbackConsultationUpdateDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            userId = userId,
            consultationUpdateId = feedbackInserting.consultationUpdateId,
            isPositive = toInt(feedbackInserting.isPositive),
            createdDate = createdDate,
            updatedDate = createdDate,
        )
    }

    fun toStats(dtos: List<FeedbackConsultationUpdateStatsDTO>): FeedbackConsultationUpdateStats {
        val positiveStatCount = dtos
            .filter { dto -> dto.hasPositiveValue == IS_POSITIVE_TRUE_VALUE }
            .sumOf { it.responseCount }
        val totalResponseCount = positiveStatCount + dtos
            .filter { dto -> dto.hasPositiveValue == IS_POSITIVE_FALSE_VALUE }
            .sumOf { it.responseCount }

        val (positiveRatio, negativeRatio) = if (totalResponseCount > 0) {
            val positiveRatio = (positiveStatCount * 100.0 / totalResponseCount).roundToInt()
            (positiveRatio to (100 - positiveRatio))
        } else (0 to 0)

        return FeedbackConsultationUpdateStats(
            positiveRatio = positiveRatio,
            negativeRatio = negativeRatio,
            responseCount = totalResponseCount,
        )
    }

    fun updateFeedback(dto: FeedbackConsultationUpdateDTO, isPositive: Boolean): FeedbackConsultationUpdateDTO {
        return dto.copy(
            isPositive = toInt(isPositive),
            updatedDate = Date(),
        )
    }

    private fun toInt(isPositive: Boolean) = if (isPositive) {
        IS_POSITIVE_TRUE_VALUE
    } else {
        IS_POSITIVE_FALSE_VALUE
    }

}

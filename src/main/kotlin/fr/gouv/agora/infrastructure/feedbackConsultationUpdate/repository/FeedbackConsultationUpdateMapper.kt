package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateDTO
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
        val consultationUpdateId = feedbackInserting.consultationUpdateId.toUuidOrNull() ?: return null

        return FeedbackConsultationUpdateDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            userId = userId,
            consultationUpdateId = consultationUpdateId,
            isPositive = if (feedbackInserting.isPositive) {
                IS_POSITIVE_TRUE_VALUE
            } else {
                IS_POSITIVE_FALSE_VALUE
            },
            createdDate = Date(),
        )
    }

    fun toStats(rawStats: Map<Int, Int>): FeedbackConsultationUpdateStats {
        val filteredStats =
            rawStats.filterKeys { key -> key == IS_POSITIVE_TRUE_VALUE || key == IS_POSITIVE_FALSE_VALUE }
        val responseCount = filteredStats.values.sum()

        val (positiveRatio, negativeRatio) = if (responseCount > 0) {
            val positiveRatio = ((filteredStats[IS_POSITIVE_TRUE_VALUE] ?: 0) * 100.0 / responseCount).roundToInt()
            (positiveRatio to (100 - positiveRatio))
        } else (0 to 0)

        return FeedbackConsultationUpdateStats(
            positiveRatio = positiveRatio,
            negativeRatio = negativeRatio,
            responseCount = responseCount,
        )
    }

}

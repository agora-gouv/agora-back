package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import org.springframework.stereotype.Component
import java.util.*

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

}

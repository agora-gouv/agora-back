package fr.gouv.agora.usecase.appFeedback.repository

import fr.gouv.agora.domain.AppFeedbackInserting
import fr.gouv.agora.domain.AppFeedbackType
import fr.gouv.agora.infrastructure.appFeedback.dto.AppFeedbackDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class AppFeedbackMapper {

    fun toDto(domain: AppFeedbackInserting): AppFeedbackDTO? {
        return domain.userId.toUuidOrNull()?.let { userUUID ->
            AppFeedbackDTO(
                id = UuidUtils.NOT_FOUND_UUID,
                userId = userUUID,
                createdDate = Date(),
                type = when (domain.type) {
                    AppFeedbackType.BUG -> "bug"
                    AppFeedbackType.FEATURE_REQUEST -> "feature"
                    AppFeedbackType.COMMENT -> "comment"
                },
                description = domain.description,
                deviceModel = domain.deviceInfo?.model,
                osVersion = domain.deviceInfo?.osVersion,
                appVersion = domain.deviceInfo?.appVersion,
            )
        }
    }

}

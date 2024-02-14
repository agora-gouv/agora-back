package fr.gouv.agora.infrastructure.appFeedback

import fr.gouv.agora.domain.AppFeedbackDeviceInfo
import fr.gouv.agora.domain.AppFeedbackInserting
import fr.gouv.agora.domain.AppFeedbackType
import org.springframework.stereotype.Component

@Component
class AppFeedbackJsonMapper {

    private companion object {
        private const val FEEDBACK_TYPE_BUG = "bug"
        private const val FEEDBACK_TYPE_FEATURE_REQUEST = "feature"
        private const val FEEDBACK_TYPE_COMMENT = "comment"
    }

    fun toDomain(json: AppFeedbackJson, userId: String): AppFeedbackInserting? {
        val type = when (json.type.lowercase()) {
            FEEDBACK_TYPE_BUG -> AppFeedbackType.BUG
            FEEDBACK_TYPE_FEATURE_REQUEST -> AppFeedbackType.FEATURE_REQUEST
            FEEDBACK_TYPE_COMMENT -> AppFeedbackType.COMMENT
            else -> null
        }

        return type?.let {
            AppFeedbackInserting(
                userId = userId,
                type = type,
                description = json.description,
                deviceInfo = json.deviceInfo?.let { deviceInfo ->
                    AppFeedbackDeviceInfo(
                        model = deviceInfo.model,
                        osVersion = deviceInfo.osVersion,
                        appVersion = deviceInfo.appVersion,
                    )
                }
            )
        }
    }

}

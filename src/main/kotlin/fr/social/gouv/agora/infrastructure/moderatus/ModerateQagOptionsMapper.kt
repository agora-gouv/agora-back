package fr.social.gouv.agora.infrastructure.moderatus

import fr.social.gouv.agora.usecase.moderatus.ModerateQagOptions
import org.springframework.stereotype.Component

@Component
class ModerateQagOptionsMapper {

    sealed class Result {
        data class Success(val options: ModerateQagOptions) : Result()
        data class Error(val errorMessage: String) : Result()
    }

    fun toModerateQagOptions(
        qagId: String,
        userId: String,
        status: String,
        reason: String?,
        shouldDeleteFlag: Int?,
    ): Result {
        val isAccepted = when (status) {
            "OK" -> true
            "NOK" -> false
            else -> return Result.Error(errorMessage = "Status invalide")
        }

        return Result.Success(
            ModerateQagOptions(
                qagId = qagId,
                userId = userId,
                isAccepted = isAccepted,
                reason = reason,
                shouldDelete = !isAccepted && shouldDeleteFlag == 1,
            )
        )
    }

}


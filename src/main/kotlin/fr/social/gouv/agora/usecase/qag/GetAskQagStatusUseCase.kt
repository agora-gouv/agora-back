package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.*
import java.util.*

@Service
class GetAskQagStatusUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
) {
    fun getAskQagStatus(userId: String): AskQagStatus {
        return if (!featureFlagsRepository.getFeatureFlags().isAskQuestionEnabled) {
            AskQagStatus.FEATURE_DISABLED
        } else {
            val latestQagByUser =
                qagInfoRepository.getAllQagInfo().filter { qagInfo -> qagInfo.userId == userId }.maxByOrNull { it.date }
            when {
                latestQagByUser == null -> AskQagStatus.ENABLED
                isDateWithinTheWeek(latestQagByUser.date) -> AskQagStatus.WEEKLY_LIMIT_REACHED
                else -> AskQagStatus.ENABLED
            }
        }
    }

    private fun isDateWithinTheWeek(postDate: Date): Boolean {
        val currentDate = LocalDate.now()
        val mondayOfWeekDateTime = LocalDateTime.of(currentDate.with(DayOfWeek.MONDAY), LocalTime.MIN)
        val sundayOfWeekDateTime = LocalDateTime.of(currentDate.with(DayOfWeek.SUNDAY), LocalTime.MAX)
        val dateToCheck = postDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        return !dateToCheck.isBefore(mondayOfWeekDateTime) && !dateToCheck.isAfter(sundayOfWeekDateTime)
    }
}

enum class AskQagStatus { FEATURE_DISABLED, WEEKLY_LIMIT_REACHED, ENABLED }

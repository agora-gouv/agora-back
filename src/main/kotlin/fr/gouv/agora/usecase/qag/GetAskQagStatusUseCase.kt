package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.AskQagStatusCacheRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

@Service
class GetAskQagStatusUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val askQagStatusCacheRepository: AskQagStatusCacheRepository,
    private val clock: Clock,
) {
    fun getAskQagStatus(userId: String): AskQagStatus {
        return if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.AskQuestion)) {
            AskQagStatus.FEATURE_DISABLED
        } else {
            val cachedStatus = askQagStatusCacheRepository.getAskQagStatus(userId = userId)
            if (cachedStatus != null) return cachedStatus

            val latestQagByUser = qagInfoRepository.getUserLastQagInfo(userId = userId)
            when {
                latestQagByUser == null -> AskQagStatus.ENABLED
                isDateWithinTheWeek(latestQagByUser.date) -> AskQagStatus.WEEKLY_LIMIT_REACHED
                else -> AskQagStatus.ENABLED
            }.also {
                askQagStatusCacheRepository.initAskQagStatus(userId = userId, status = it)
            }
        }
    }

    private fun isDateWithinTheWeek(postDate: Date): Boolean {
        val postDateLocalDateTime = postDate.toLocalDateTime()
        val currentDate = LocalDateTime.now(clock)
        val mondayThisWeek = currentDate.with(DayOfWeek.MONDAY).withHour(14).withMinute(0).withSecond(0)

        val (previousMonday, nextMonday) = when {
            currentDate < mondayThisWeek -> mondayThisWeek.minusDays(7) to mondayThisWeek
            currentDate > mondayThisWeek -> mondayThisWeek to mondayThisWeek.plusDays(7)
            else -> mondayThisWeek to mondayThisWeek.plusDays(7) // equals case
        }

        return postDateLocalDateTime >= previousMonday && postDateLocalDateTime < nextMonday
    }
}

enum class AskQagStatus { FEATURE_DISABLED, WEEKLY_LIMIT_REACHED, ENABLED }

package fr.gouv.agora.usecase.qag

import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Date

@Service
class GetAskQagStatusUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val clock: Clock,
) {
    fun getAskQagStatus(userId: String): AskQagStatus {
        val latestQagByUser = qagInfoRepository.getUserLastQagInfo(userId = userId)
        return when {
            latestQagByUser == null -> AskQagStatus.ENABLED
            isDateWithinTheWeek(latestQagByUser.date) -> AskQagStatus.WEEKLY_LIMIT_REACHED
            else -> AskQagStatus.ENABLED
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

package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.usecase.errorMessages.repository.ErrorMessagesRepository
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.*
import java.util.*

@Service
class GetQagErrorTextUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val errorMessagesRepository: ErrorMessagesRepository,
) {
    fun getGetQagErrorText(userId: String): String? {
        return if (!featureFlagsRepository.getFeatureFlags().isAskQuestionEnabled) {
            errorMessagesRepository.getQagDisabledErrorMessage()
        } else {
            val latestQagByUser =
                qagInfoRepository.getAllQagInfo().filter { qagInfo -> qagInfo.userId == userId }.maxByOrNull { it.date }
            when {
                latestQagByUser == null -> null
                isDateWithinTheWeek(latestQagByUser.date) -> errorMessagesRepository.getQagErrorMessageOneByWeek()
                else -> null
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

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
    companion object {
        private const val ERROR_TEXT_WITHIN_THE_WEEK =
            "Vous avez déjà posé une question au Gouvernement cette semaine. " +
                    "Pendant cette phase d’expérimentation, l’appli est limitée à une question par semaine pour chaque utilisateur. " +
                    "Nous augmenterons le nombre de questions possibles dès que nos capacités de modération le permettront. " +
                    "Rendez-vous la semaine prochaine pour votre question suivante. D’ici là, n’hésitez pas à soutenir les questions des utilisateurs, sans limite !"
    }

    fun getGetQagErrorText(userId: String): String? {
        return if (!featureFlagsRepository.getFeatureFlags().isAskQuestionEnabled) {
            errorMessagesRepository.getQagErrorMessageFromSystemEnv()
        } else {
            val latestQagByUser =
                qagInfoRepository.getAllQagInfo().filter { qagInfo -> qagInfo.userId == userId }.maxByOrNull { it.date }
            if (latestQagByUser == null)
                null
            else {
                if (testDateWithinTheWeek(latestQagByUser.date))
                    ERROR_TEXT_WITHIN_THE_WEEK
                else
                    null
            }
        }
    }

    private fun testDateWithinTheWeek(postDate: Date): Boolean {
        val currentDate = LocalDate.now()
        val mondayOfWeekDateTime = LocalDateTime.of(currentDate.with(DayOfWeek.MONDAY), LocalTime.MIN)
        val sundayOfWeekDateTime = LocalDateTime.of(currentDate.with(DayOfWeek.SUNDAY), LocalTime.MAX)
        val dateToCheck = postDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        return !dateToCheck.isBefore(mondayOfWeekDateTime) && !dateToCheck.isAfter(sundayOfWeekDateTime)
    }
}

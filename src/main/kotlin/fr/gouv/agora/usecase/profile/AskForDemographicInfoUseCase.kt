package fr.gouv.agora.usecase.profile

import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.profile.AskDemographicInfoState.ASK_DEMOGRAPHIC_INFO_DELAY_FINISHED
import fr.gouv.agora.usecase.profile.AskDemographicInfoState.ASK_DEMOGRAPHIC_INFO_DELAY_UNFINISHED
import fr.gouv.agora.usecase.profile.AskDemographicInfoState.FIRST_TIME_ASK_DEMOGRAPHIC_INFO
import fr.gouv.agora.usecase.profile.AskDemographicInfoState.HAS_DEMOGRAPHIC_INFO
import fr.gouv.agora.usecase.profile.AskDemographicInfoState.HAS_NOT_ANSWERED_ENOUGH_CONSULTATIONS
import fr.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AskForDemographicInfoUseCase(
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val profileRepository: ProfileRepository,
    private val demographicInfoAskDateRepository: DemographicInfoAskDateRepository,
) {

    companion object {
        private const val MINIMUM_CONSULTATION_ANSWERED_REQUIREMENT = 1
        private const val DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO = 30
    }

    fun askForDemographicInfo(userId: String): Boolean {
        return when (processAskDemographicInfoState(userId)) {
            HAS_DEMOGRAPHIC_INFO -> false
            HAS_NOT_ANSWERED_ENOUGH_CONSULTATIONS -> false
            FIRST_TIME_ASK_DEMOGRAPHIC_INFO -> {
                demographicInfoAskDateRepository.insertDate(userId)
                true
            }

            ASK_DEMOGRAPHIC_INFO_DELAY_FINISHED -> {
                demographicInfoAskDateRepository.insertDate(userId)
                true
            }

            ASK_DEMOGRAPHIC_INFO_DELAY_UNFINISHED -> false
        }
    }

    private fun processAskDemographicInfoState(userId: String): AskDemographicInfoState {
        val profile = profileRepository.getProfile(userId)
        if (profile != null && profile.isCompleted()) {
            return HAS_DEMOGRAPHIC_INFO
        }

        if (userAnsweredConsultationRepository.getAnsweredConsultationIds(userId).size < MINIMUM_CONSULTATION_ANSWERED_REQUIREMENT) {
            return HAS_NOT_ANSWERED_ENOUGH_CONSULTATIONS
        }

        val askDate = demographicInfoAskDateRepository.getDate(userId)

        return when {
            askDate == null -> FIRST_TIME_ASK_DEMOGRAPHIC_INFO
            isAskDateDelayFinished(askDate) -> ASK_DEMOGRAPHIC_INFO_DELAY_FINISHED
            else -> ASK_DEMOGRAPHIC_INFO_DELAY_UNFINISHED
        }
    }

    private fun isAskDateDelayFinished(askDate: LocalDate) =
        LocalDate.now().isAfter(askDate.plusDays(DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO.toLong()))
}

private enum class AskDemographicInfoState {
    HAS_DEMOGRAPHIC_INFO,
    HAS_NOT_ANSWERED_ENOUGH_CONSULTATIONS,
    FIRST_TIME_ASK_DEMOGRAPHIC_INFO,
    ASK_DEMOGRAPHIC_INFO_DELAY_FINISHED,
    ASK_DEMOGRAPHIC_INFO_DELAY_UNFINISHED
}

package fr.social.gouv.agora.usecase.profile

import fr.social.gouv.agora.usecase.profile.AskDemographicInfoState.*
import fr.social.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AskForDemographicInfoUseCase(
    private val profileRepository: ProfileRepository,
    private val demographicInfoAskDateRepository: DemographicInfoAskDateRepository,
) {

    companion object {
        private const val DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO = 30
    }

    fun askForDemographicInfo(userId: String): Boolean {
        return when (processAskDemographicInfoState(userId)) {
            HAS_DEMOGRAPHIC_INFO -> false
            FIRST_TIME_ASK_DEMOGRAPHIC_INFO -> {
                demographicInfoAskDateRepository.insertDate(userId)
                true
            }
            ASK_DEMOGRAPHIC_INFO_DELAY_FINISHED -> {
                demographicInfoAskDateRepository.updateDate(userId)
                true
            }
            ASK_DEMOGRAPHIC_INFO_DELAY_UNFINISHED -> false
        }
    }

    private fun processAskDemographicInfoState(userId: String): AskDemographicInfoState {
        if (profileRepository.getProfile(userId) != null) {
            return HAS_DEMOGRAPHIC_INFO
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
    FIRST_TIME_ASK_DEMOGRAPHIC_INFO,
    ASK_DEMOGRAPHIC_INFO_DELAY_FINISHED,
    ASK_DEMOGRAPHIC_INFO_DELAY_UNFINISHED
}
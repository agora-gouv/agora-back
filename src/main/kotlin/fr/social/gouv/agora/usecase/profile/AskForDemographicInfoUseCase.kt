package fr.social.gouv.agora.usecase.profile

import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class AskForDemographicInfoUseCase(
    private val profileRepository: ProfileRepository,
    private val demographicInfoAskDateRepository: DemographicInfoAskDateRepository,
    private val loginRepository: LoginRepository,
) {

    companion object {
        private const val DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO = 30
    }

    fun askForDemographicInfo(deviceId: String): Boolean {
        var askForDemographicInfo = false
        val userId = loginRepository.getUser(deviceId)?.userId
        userId?.let {
            if (profileRepository.getProfile(userId) == null) {
                val dateDemande = demographicInfoAskDateRepository.getDate(userId)
                if (dateDemande == null) {
                    askForDemographicInfo = true
                    demographicInfoAskDateRepository.insertDate(userId)
                }
                else {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val dateDemandeLocalDate = LocalDate.parse(dateDemande, formatter)
                    if (compareDateDemandeWithSYSDate(dateDemandeLocalDate)) {
                        askForDemographicInfo = true
                        demographicInfoAskDateRepository.updateDate(userId)
                    }
                }
            }
        }
        return askForDemographicInfo
    }

    private fun compareDateDemandeWithSYSDate(dateDemandeLocalDate: LocalDate) = LocalDate.now()
        .isAfter(
            dateDemandeLocalDate.plusDays(
                DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO.toLong()
            )
        )
}
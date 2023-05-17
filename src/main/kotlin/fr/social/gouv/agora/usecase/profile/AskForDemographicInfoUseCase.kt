package fr.social.gouv.agora.usecase.profile

import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.profile.repository.DateDemandeRepository
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class AskForDemographicInfoUseCase(
    private val profileRepository: ProfileRepository,
    private val dateDemandeRepository: DateDemandeRepository,
    private val loginRepository: LoginRepository,
) {

    companion object {
        private const val NOMBRE_JOUR_DEMANDE_INFO_PROFIL = 30
    }

    fun askForDemographicInfo(deviceId: String): Boolean {
        var askForDemographicInfo = false
        val userId = loginRepository.getUser(deviceId)?.userId
        userId?.let {
            if (profileRepository.getProfile(userId) == null) {
                val dateDemande = dateDemandeRepository.getDate(UUID.fromString(userId))
                if (dateDemande == null)
                    askForDemographicInfo = true
                else {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val dateDemandeLocalDate = LocalDate.parse(dateDemande, formatter)
                    if (LocalDate.now()
                            .isAfter(
                                dateDemandeLocalDate.plusDays(
                                    NOMBRE_JOUR_DEMANDE_INFO_PROFIL.toLong()
                                )
                            )
                    ) {
                        askForDemographicInfo = true
                        dateDemandeRepository.updateDate(UUID.fromString(userId))
                    }
                }
            }
        }
        return askForDemographicInfo
    }
}
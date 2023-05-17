package fr.social.gouv.agora.usecase.profile

import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class AskForDemographicInfoUseCase (
    private val repository: ProfileRepository,
    private val loginRepository: LoginRepository,){
    fun askForDemographicInfo(deviceId: String): Boolean {
        return loginRepository.getUser(deviceId)?.let { repository.askForDemographicInfo(it.userId) }?:false
    }
}
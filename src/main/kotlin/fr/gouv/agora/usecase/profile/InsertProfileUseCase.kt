package fr.gouv.agora.usecase.profile

import fr.gouv.agora.domain.ProfileInserting
import fr.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.gouv.agora.usecase.profile.repository.ProfileEditResult
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class InsertProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val demographicInfoAskDateRepository: DemographicInfoAskDateRepository,
) {
    fun insertProfile(profileInserting: ProfileInserting): ProfileEditResult {
        if (profileRepository.getProfile(userId = profileInserting.userId) != null) {
            return profileRepository.updateProfile(profileInserting)
        }
        demographicInfoAskDateRepository.deleteDate(profileInserting.userId)
        return profileRepository.insertProfile(profileInserting)
    }
}

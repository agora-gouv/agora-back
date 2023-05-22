package fr.social.gouv.agora.usecase.profile

import fr.social.gouv.agora.domain.Profile
import fr.social.gouv.agora.domain.ProfileInserting
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.social.gouv.agora.usecase.profile.repository.ProfileInsertionResult
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class InsertProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val demographicInfoAskDateRepository: DemographicInfoAskDateRepository,
    private val loginRepository: LoginRepository,
) {
    fun insertProfile(deviceId: String, profile: Profile): ProfileInsertionResult {
        val userId = loginRepository.getUser(deviceId)?.userId
        userId?.let {
            profileRepository.insertProfile(
                ProfileInserting(
                    gender = profile.gender,
                    yearOfBirth = profile.yearOfBirth,
                    department = profile.department,
                    cityType = profile.cityType,
                    jobCategory = profile.jobCategory,
                    voteFrequency = profile.voteFrequency,
                    publicMeetingFrequency = profile.publicMeetingFrequency,
                    consultationFrequency = profile.consultationFrequency,
                    userId = userId,
                )
            )
            demographicInfoAskDateRepository.deleteDate(userId)
        }
        return ProfileInsertionResult.FAILURE
    }
}
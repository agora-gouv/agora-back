package fr.gouv.agora.usecase.profile

import fr.gouv.agora.domain.Profile
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class GetProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    fun getProfile(userId: String): Profile {
        return profileRepository.getProfile(userId) ?: createEmptyProfile()
    }

    private fun createEmptyProfile() = Profile(
        gender = null,
        yearOfBirth = null,
        department = null,
        cityType = null,
        jobCategory = null,
        voteFrequency = null,
        publicMeetingFrequency = null,
        consultationFrequency = null,
        primaryDepartment = null,
        secondaryDepartment = null,
    )
}

package fr.gouv.agora.usecase.profile.repository

import fr.gouv.agora.domain.Profile
import fr.gouv.agora.domain.ProfileInserting

interface ProfileRepository {
    fun getProfile(userId: String): Profile?
    fun updateProfile(profileInserting: ProfileInserting): ProfileEditResult
    fun insertProfile(profileInserting: ProfileInserting): ProfileEditResult
}

enum class ProfileEditResult {
    SUCCESS, FAILURE
}
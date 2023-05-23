package fr.social.gouv.agora.usecase.profile.repository

import fr.social.gouv.agora.domain.Profile
import fr.social.gouv.agora.domain.ProfileInserting

interface ProfileRepository {
    fun getProfile(userId: String): Profile?
    fun insertProfile(profileInserting: ProfileInserting): ProfileInsertionResult
}

enum class ProfileInsertionResult {
    SUCCESS, FAILURE
}
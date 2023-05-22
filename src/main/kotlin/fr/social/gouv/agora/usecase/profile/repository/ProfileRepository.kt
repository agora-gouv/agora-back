package fr.social.gouv.agora.usecase.profile.repository

import fr.social.gouv.agora.domain.Profile

interface ProfileRepository {
    fun getProfile(userId: String): Profile?
}
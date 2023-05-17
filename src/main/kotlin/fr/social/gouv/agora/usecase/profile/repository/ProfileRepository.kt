package fr.social.gouv.agora.usecase.profile.repository

interface ProfileRepository {
    fun askForDemographicInfo(userId: String?): Boolean
}
package fr.social.gouv.agora.usecase.profile.repository

interface DemographicInfoAskDateRepository {
    fun getDate(userId: String): String?
    fun insertDate(userId: String)
    fun updateDate(userId: String)
    fun deleteDate(userId: String)
}
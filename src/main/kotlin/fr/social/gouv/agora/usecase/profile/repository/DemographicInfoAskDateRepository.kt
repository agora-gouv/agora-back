package fr.social.gouv.agora.usecase.profile.repository

import java.time.LocalDate

interface DemographicInfoAskDateRepository {
    fun getDate(userId: String): LocalDate?
    fun insertDate(userId: String)
    fun deleteDate(userId: String)
}
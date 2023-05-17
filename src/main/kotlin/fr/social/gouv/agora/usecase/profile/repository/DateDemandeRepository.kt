package fr.social.gouv.agora.usecase.profile.repository

import java.util.*

interface DateDemandeRepository {
    fun getDate(userUUID: UUID): String?
    fun insertDate(userUUID: UUID)
    fun updateDate(userUUID: UUID)
    fun deleteDate(userId: UUID)
}
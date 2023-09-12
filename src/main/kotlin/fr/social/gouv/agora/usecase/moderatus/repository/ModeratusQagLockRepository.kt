package fr.social.gouv.agora.usecase.moderatus.repository

interface ModeratusQagLockRepository {
    fun getLockedQagIds(): List<String>
    fun addLockedIds(lockedQagIds: List<String>)
    fun removeLockedQagId(qagId: String)
}
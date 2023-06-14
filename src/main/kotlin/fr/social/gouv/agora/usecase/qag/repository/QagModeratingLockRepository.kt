package fr.social.gouv.agora.usecase.qag.repository

interface QagModeratingLockRepository {
    fun getLockedQagList(userId: String): List<String>
    fun setLockedQagList(userId: String, qagList: List<String>)
    fun isQagIdListLocked(qagList: List<String>): Boolean
    fun deleteQagLockedList(userId: String)
}
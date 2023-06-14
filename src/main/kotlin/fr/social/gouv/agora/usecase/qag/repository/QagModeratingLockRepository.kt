package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.infrastructure.qag.repository.QagLockList

interface QagModeratingLockRepository {
    fun getLockedQagList(userId: String): QagLockList?
    fun setLockedQagList(userId: String, qagList: List<String>)
    fun isQagIdListLocked(qagList: List<String>): Boolean
    fun deleteQagLockedList(userId: String)
}
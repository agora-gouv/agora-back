package fr.social.gouv.agora.usecase.qag.repository

interface QagModeratingLockRepository {
    fun isQagLocked(qagId: String): Boolean
    fun setQagLocked(qagId: String)
}


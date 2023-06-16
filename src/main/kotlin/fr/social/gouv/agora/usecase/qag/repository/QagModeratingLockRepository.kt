package fr.social.gouv.agora.usecase.qag.repository

interface QagModeratingLockRepository {
    fun getUserIdForQagLocked(qagId: String): String?
    fun setQagLocked(qagId: String, userId: String)
}


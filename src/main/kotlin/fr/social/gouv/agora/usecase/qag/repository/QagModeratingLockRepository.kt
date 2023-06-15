package fr.social.gouv.agora.usecase.qag.repository

interface QagModeratingLockRepository {
    fun getQagLocked(qagId: String): String?
    fun setQagLocked(qagId: String, userId: String)
}


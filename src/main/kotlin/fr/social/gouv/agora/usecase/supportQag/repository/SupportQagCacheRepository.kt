package fr.social.gouv.agora.usecase.supportQag.repository

interface SupportQagCacheRepository {
    fun getUserSupportedQagIds(userId: String): List<String>?
    fun initUserSupportedQagIds(userId: String, userSupportedQagIds: List<String>)
    fun addSupportedQagIds(userId: String, qagId: String)
    fun removeSupportedQagIds(userId: String, qagId: String)
}
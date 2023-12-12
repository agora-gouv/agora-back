package fr.gouv.agora.usecase.supportQag.repository

interface GetSupportQagRepository {
    fun getUserSupportedQags(userId: String, thematiqueId: String?): List<String>
    fun isQagSupported(userId: String, qagId: String): Boolean
}
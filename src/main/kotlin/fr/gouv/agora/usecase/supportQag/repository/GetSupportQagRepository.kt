package fr.gouv.agora.usecase.supportQag.repository

interface GetSupportQagRepository {
    fun getUserSupportedQags(userId: String): List<String>
    fun isQagSupported(userId: String, qagId: String): Boolean
    fun getSupportedQagCount(userId: String, thematiqueId: String?): Int
}
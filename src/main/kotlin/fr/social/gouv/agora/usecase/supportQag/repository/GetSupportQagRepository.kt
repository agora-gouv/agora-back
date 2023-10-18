package fr.social.gouv.agora.usecase.supportQag.repository

interface GetSupportQagRepository {
    fun getUserSupportedQags(userId: String): List<String>
}
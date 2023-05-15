package fr.social.gouv.agora.usecase.qag.repository

interface QagSupportedListRepository {
    fun getQagSupportedList(thematiqueId: String?, userId: String): List<QagInfo>
    fun deleteQagSupportedList(thematiqueId: String, userId: String)
}
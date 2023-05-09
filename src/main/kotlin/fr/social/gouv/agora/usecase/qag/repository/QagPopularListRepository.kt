package fr.social.gouv.agora.usecase.qag.repository

interface QagPopularListRepository {
    fun getQagPopularList(thematiqueId: String?): List<QagInfo>
}
package fr.social.gouv.agora.usecase.qag.repository

interface QagLatestListRepository {
    fun getQagLatestList(thematiqueId: String?): List<QagInfo>
    fun deleteQagLatestList(thematiqueId: String)
}
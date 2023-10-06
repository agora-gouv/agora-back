package fr.social.gouv.agora.usecase.qagPreview.repository

import fr.social.gouv.agora.usecase.qag.QagWithSupportCount

interface QagPreviewPageCacheRepository {

    fun getQagPopularList(thematiqueId: String?): List<QagWithSupportCount>?
    fun initQagPopularList(thematiqueId: String?, qagList: List<QagWithSupportCount>)
    fun evictQagPopularList(thematiqueId: String?)

    fun getQagLatestList(thematiqueId: String?): List<QagWithSupportCount>?
    fun initQagLatestList(thematiqueId: String?, qagList: List<QagWithSupportCount>)
    fun evictQagLatestList(thematiqueId: String?)

    fun getQagSupportedList(userId: String, thematiqueId: String?): List<QagWithSupportCount>?
    fun initQagSupportedList(userId: String, thematiqueId: String?, qagList: List<QagWithSupportCount>)
    fun evictQagSupportedList(userId: String, thematiqueId: String?)

    fun clear()

}
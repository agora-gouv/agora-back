package fr.gouv.agora.usecase.qag.repository

import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount

interface QagPreviewCacheRepository {

    fun getQagPopularList(thematiqueId: String?): List<QagWithSupportCount>?
    fun initQagPopularList(thematiqueId: String?, qagList: List<QagWithSupportCount>)
    fun incrementQagPopularSupportCount(thematiqueId: String?, qagId: String)
    fun decrementQagPopularSupportCount(thematiqueId: String?, qagId: String)
    fun evictQagPopularList(thematiqueId: String?)

    fun getQagLatestList(thematiqueId: String?): List<QagWithSupportCount>?
    fun initQagLatestList(thematiqueId: String?, qagList: List<QagWithSupportCount>)
    fun incrementQagLatestSupportCount(thematiqueId: String?, qagId: String)
    fun decrementQagLatestSupportCount(thematiqueId: String?, qagId: String)
    fun evictQagLatestList(thematiqueId: String?)

    fun getQagSupportedList(userId: String, thematiqueId: String?): List<QagWithSupportCount>?
    fun initQagSupportedList(userId: String, thematiqueId: String?, qagList: List<QagWithSupportCount>)
    fun incrementSupportedSupportCount(userId: String, thematiqueId: String?, qagId: String)
    fun decrementSupportedSupportCount(userId: String, thematiqueId: String?, qagId: String)
    fun evictQagSupportedList(userId: String, thematiqueId: String?)

    fun clear()

}
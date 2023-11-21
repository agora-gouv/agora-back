package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount

interface QagListsCacheRepository {

    fun getQagPopularList(thematiqueId: String?, pageNumber: Int): Pair<Int, List<QagWithSupportCount>>?
    fun initQagPopularList(thematiqueId: String?, pageNumber: Int, maxPageCount: Int, qags: List<QagWithSupportCount>)
    fun evictQagPopularList(thematiqueId: String?, pageNumber: Int)

    fun getQagLatestList(thematiqueId: String?, pageNumber: Int): Pair<Int, List<QagWithSupportCount>>?
    fun initQagLatestList(thematiqueId: String?, pageNumber: Int, maxPageCount: Int, qags: List<QagWithSupportCount>)
    fun evictQagLatestList(thematiqueId: String?, pageNumber: Int)

    fun getQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int): Pair<Int, List<QagWithSupportCount>>?
    fun initQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int, maxPageCount: Int, qags: List<QagWithSupportCount>)
    fun evictQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int)

    fun clear()

}
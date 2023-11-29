package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.infrastructure.qag.repository.QagListWithMaxPageCount

interface QagListsCacheRepository {

    fun getQagPopularList(thematiqueId: String?, pageNumber: Int): QagListWithMaxPageCount?
    fun initQagPopularList(thematiqueId: String?, pageNumber: Int, qagListWithMaxPageCount: QagListWithMaxPageCount)
    fun incrementQagPopularSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String)
    fun decrementQagPopularSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String)
    fun evictQagPopularList(thematiqueId: String?, pageNumber: Int)

    fun getQagLatestList(thematiqueId: String?, pageNumber: Int): QagListWithMaxPageCount?
    fun initQagLatestList(thematiqueId: String?, pageNumber: Int, qagListWithMaxPageCount: QagListWithMaxPageCount)
    fun incrementQagLatestSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String)
    fun decrementQagLatestSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String)
    fun evictQagLatestList(thematiqueId: String?, pageNumber: Int)

    fun getQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int): QagListWithMaxPageCount?
    fun initQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int, qagListWithMaxPageCount: QagListWithMaxPageCount)
    fun incrementSupportedSupportCount(userId: String, thematiqueId: String?, pageNumber: Int, qagId: String)
    fun decrementSupportedSupportCount(userId: String, thematiqueId: String?, pageNumber: Int, qagId: String)
    fun evictQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int)

    fun clear()

}
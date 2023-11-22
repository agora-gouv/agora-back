package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.infrastructure.qag.repository.QagListWithMaxPageCount

interface QagListsCacheRepository {

    fun getQagPopularList(thematiqueId: String?, pageNumber: Int): QagListWithMaxPageCount?
    fun initQagPopularList(thematiqueId: String?, pageNumber: Int, qagListWithMaxPageCount: QagListWithMaxPageCount)
    fun evictQagPopularList(thematiqueId: String?, pageNumber: Int)

    fun getQagLatestList(thematiqueId: String?, pageNumber: Int): QagListWithMaxPageCount?
    fun initQagLatestList(thematiqueId: String?, pageNumber: Int, qagListWithMaxPageCount: QagListWithMaxPageCount)
    fun evictQagLatestList(thematiqueId: String?, pageNumber: Int)

    fun getQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int): QagListWithMaxPageCount?
    fun initQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int, qagListWithMaxPageCount: QagListWithMaxPageCount)
    fun evictQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int)

    fun clear()

}
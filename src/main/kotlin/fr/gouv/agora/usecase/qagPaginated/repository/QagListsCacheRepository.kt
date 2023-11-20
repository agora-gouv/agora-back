package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.usecase.qagPaginated.QagsAndMaxPageCount

interface QagListsCacheRepository {

    fun getQagPopularList(thematiqueId: String?, pageNumber: Int): QagsAndMaxPageCount?
    fun initQagPopularList(thematiqueId: String?, pageNumber: Int, qagsAndMaxPageCount: QagsAndMaxPageCount)
    fun evictQagPopularList(thematiqueId: String?, pageNumber: Int)

    fun getQagLatestList(thematiqueId: String?, pageNumber: Int): QagsAndMaxPageCount?
    fun initQagLatestList(thematiqueId: String?, pageNumber: Int, qagsAndMaxPageCount: QagsAndMaxPageCount)
    fun evictQagLatestList(thematiqueId: String?, pageNumber: Int)

    fun getQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int): QagsAndMaxPageCount?
    fun initQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int, qagsAndMaxPageCount: QagsAndMaxPageCount)
    fun evictQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int)

    fun clear()

}
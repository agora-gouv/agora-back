package fr.gouv.agora.usecase.responseQag.repository

import fr.gouv.agora.usecase.responseQag.ResponseQagPreviewList

interface ResponseQagPreviewCacheRepository {
    fun getResponseQagPreviewList(): ResponseQagPreviewList?
    fun initResponseQagPreviewList(responseList: ResponseQagPreviewList)
    fun evictResponseQagPreviewList()
}
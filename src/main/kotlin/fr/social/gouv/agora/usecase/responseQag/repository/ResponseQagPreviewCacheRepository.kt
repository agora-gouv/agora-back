package fr.social.gouv.agora.usecase.responseQag.repository

import fr.social.gouv.agora.usecase.responseQag.ResponseQagPreviewList

interface ResponseQagPreviewCacheRepository {
    fun getResponseQagPreviewList(): ResponseQagPreviewList?
    fun initResponseQagPreviewList(responseList: ResponseQagPreviewList)
    fun evictResponseQagPreviewList()
}
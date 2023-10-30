package fr.gouv.agora.usecase.qag.repository

import fr.gouv.agora.usecase.qag.AskQagStatus

interface AskQagStatusCacheRepository {
    fun getAskQagStatus(userId: String): AskQagStatus?
    fun initAskQagStatus(userId: String, status: AskQagStatus)
    fun evictAskQagStatus(userId: String)
    fun clear()
}
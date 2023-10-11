package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.Qag
import org.springframework.stereotype.Component

interface QagCacheRepository {
    fun getQag(qagId: String): QagCacheResult
    fun initQagNotFound(qagId: String)
    fun initQag(qag: Qag)
    fun evictQag(qagId: String)
    fun clear()
}

sealed class QagCacheResult {
    data class CachedQag(val qag: Qag): QagCacheResult()
    object QagNotFount: QagCacheResult()
    object QagCacheNotInitialized: QagCacheResult()
}
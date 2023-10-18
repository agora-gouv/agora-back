package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagDetails

interface QagDetailsCacheRepository {
    fun getQag(qagId: String): QagDetailsCacheResult
    fun initQagNotFound(qagId: String)
    fun initQag(qagDetails: QagDetails)
    fun evictQag(qagId: String)
    fun clear()
}

sealed class QagDetailsCacheResult {
    data class CachedQagDetails(val qagDetails: QagDetails): QagDetailsCacheResult()
    object QagDetailsNotFount: QagDetailsCacheResult()
    object QagDetailsCacheNotInitialized: QagDetailsCacheResult()
}
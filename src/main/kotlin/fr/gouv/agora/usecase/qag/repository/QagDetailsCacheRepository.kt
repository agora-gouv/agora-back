package fr.gouv.agora.usecase.qag.repository

import fr.gouv.agora.domain.QagDetails

interface QagDetailsCacheRepository {
    fun getQag(qagId: String): QagDetailsCacheResult
    fun initQagNotFound(qagId: String)
    fun initQag(qagDetails: QagDetails)
    fun incrementSupportCount(qagId: String)
    fun decrementSupportCount(qagId: String)
    fun evictQag(qagId: String)
    fun clear()
}

sealed class QagDetailsCacheResult {
    data class CachedQagDetails(val qagDetails: QagDetails): QagDetailsCacheResult()
    object QagDetailsNotFound: QagDetailsCacheResult()
    object QagDetailsCacheNotInitialized: QagDetailsCacheResult()
}
package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.domain.HeaderQag

interface HeaderQagCacheRepository {

    fun getHeader(filterType: String): HeaderQagCacheResult
    fun initHeaderNotFound(filterType: String)
    fun initHeader(filterType: String, headerQag: HeaderQag)
}

sealed class HeaderQagCacheResult {
    data class CachedHeaderQag(val headerQag: HeaderQag): HeaderQagCacheResult()
    object HeaderQagNotFound: HeaderQagCacheResult()
    object HeaderQagCacheNotInitialized: HeaderQagCacheResult()
}
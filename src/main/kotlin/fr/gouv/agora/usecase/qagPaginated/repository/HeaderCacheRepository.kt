package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.domain.Header

interface HeaderCacheRepository {

    fun getHeader(filterType: String): Header?
    fun initHeader(filterType: String, header: Header)
    fun evictHeader(filterType: String)

    fun clear()
}
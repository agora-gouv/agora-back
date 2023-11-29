package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.domain.HeaderQag

interface HeaderQagCacheRepository {

    fun getHeader(filterType: String): HeaderQag?
    fun initHeader(filterType: String, headerQag: HeaderQag)
}
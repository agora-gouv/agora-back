package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.domain.HeaderQag

interface HeaderQagRepository {

    fun getHeader(filterType: String): HeaderQag?

}
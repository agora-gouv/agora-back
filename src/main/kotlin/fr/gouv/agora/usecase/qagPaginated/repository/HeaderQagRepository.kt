package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.domain.HeaderQag

interface HeaderQagRepository {

    fun getLastHeader(filterType: String): HeaderQag?

}

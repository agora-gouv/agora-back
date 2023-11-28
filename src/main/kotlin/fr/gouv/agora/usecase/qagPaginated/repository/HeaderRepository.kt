package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.domain.Header

interface HeaderRepository {

    fun getHeader(filterType: String): Header?

}
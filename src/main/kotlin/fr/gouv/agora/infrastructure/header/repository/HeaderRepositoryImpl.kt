package fr.gouv.agora.infrastructure.header.repository

import fr.gouv.agora.domain.Header
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderRepository

import org.springframework.stereotype.Component

@Component
class HeaderRepositoryImpl(
    private val databaseRepository: HeaderDatabaseRepository,
    private val mapper: HeaderMapper,
) : HeaderRepository {

    override fun getHeader(filterType: String): Header? {
        return databaseRepository.getHeader(filterType)?.let(mapper::toDomain)
    }
}




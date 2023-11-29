package fr.gouv.agora.infrastructure.headerQag.repository

import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository

import org.springframework.stereotype.Component

@Component
class HeaderQagRepositoryImpl(
    private val databaseRepository: HeaderQagDatabaseRepository,
    private val mapper: HeaderQagMapper,
) : HeaderQagRepository {

    override fun getHeader(filterType: String): HeaderQag? {
        return databaseRepository.getHeader(filterType)?.let(mapper::toDomain)
    }
}




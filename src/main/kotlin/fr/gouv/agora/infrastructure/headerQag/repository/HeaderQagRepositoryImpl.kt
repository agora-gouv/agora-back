package fr.gouv.agora.infrastructure.headerQag.repository

import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class HeaderQagRepositoryImpl(
    private val headerQagStrapiRepository: HeaderQagStrapiRepository,
    private val clock: Clock,
) : HeaderQagRepository {

    override fun getLastHeader(filterType: String): HeaderQag? {
        val now = LocalDateTime.now(clock)

        return headerQagStrapiRepository.getLastHeader(filterType, now)?.let {
            HeaderQag(it.id, it.attributes.titre, it.attributes.message)
        }
    }
}

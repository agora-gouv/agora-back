package fr.gouv.agora.infrastructure.headerQag.repository

import fr.gouv.agora.domain.AgoraFeature.StrapiHeaders
import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class HeaderQagRepositoryImpl(
    private val databaseRepository: HeaderQagDatabaseRepository,
    private val headerQagStrapiRepository: HeaderQagStrapiRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val clock: Clock,
    private val mapper: HeaderQagMapper,
) : HeaderQagRepository {

    override fun getLastHeader(filterType: String): HeaderQag? {
        val now = LocalDateTime.now(clock)

        return if (featureFlagsRepository.isFeatureEnabled(StrapiHeaders)) {
            headerQagStrapiRepository.getLastHeader(filterType, now)?.let {
                HeaderQag(it.id, it.attributes.titre, it.attributes.message)
            }
        } else {
            databaseRepository.getLastHeader(filterType, now)?.let(mapper::toDomain)
        }
    }
}

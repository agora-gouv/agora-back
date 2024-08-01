package fr.gouv.agora.infrastructure.headerQag.repository

import fr.gouv.agora.domain.AgoraFeature.StrapiHeaders
import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderQagRepository
import org.springframework.stereotype.Component

@Component
class HeaderQagRepositoryImpl(
    private val databaseRepository: HeaderQagDatabaseRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val mapper: HeaderQagMapper,
) : HeaderQagRepository {

    override fun getHeader(filterType: String): HeaderQag? {
        return if (featureFlagsRepository.isFeatureEnabled(StrapiHeaders)) {
            TODO()
        } else {
            databaseRepository.getHeader(filterType)?.let(mapper::toDomain)
        }
    }
}

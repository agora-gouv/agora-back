package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.responseQag.repository.ThematiqueStrapiRepository
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component

@Component
class ThematiqueRepositoryImpl(
    private val cacheRepository: ThematiqueCacheRepository,
    private val databaseRepository: ThematiqueDatabaseRepository,
    private val strapiRepository: ThematiqueStrapiRepository,
    private val mapper: ThematiqueMapper,
    private val featureFlagsRepository: FeatureFlagsRepository,
) : ThematiqueRepository {
    override fun getThematique(thematiqueId: String): Thematique? {
        return getThematiqueList().find { it.id == thematiqueId }
    }

    override fun getThematiqueList() = when (val cacheResult = cacheRepository.getThematiqueList()) {
        is CacheListResult.CacheNotInitialized -> getThematiqueListAndCacheIt()
        is CacheListResult.CachedThematiqueList -> cacheResult.thematiqueList
    }

    internal fun getThematiqueListAndCacheIt(): List<Thematique> {
        // todo : ou ne pas faire de feature flag et faire un .unique sur l'id ?
        val thematiques = if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.Strapi)) {
            strapiRepository.getThematiques().let(mapper::toDomain)
        } else {
            databaseRepository.getThematiqueList().map(mapper::toDomain)
        }

        cacheRepository.insertThematiqueList(thematiques)

        return thematiques
    }
}

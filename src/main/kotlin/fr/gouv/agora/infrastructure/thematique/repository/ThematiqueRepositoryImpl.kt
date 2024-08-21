package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ThematiqueRepositoryImpl(
    private val cacheRepository: ThematiqueCacheRepository,
    private val databaseRepository: ThematiqueDatabaseRepository,
    private val strapiRepository: ThematiqueStrapiRepository,
    private val mapper: ThematiqueMapper,
    private val featureFlagsRepository: FeatureFlagsRepository,
) : ThematiqueRepository {
    private val logger: Logger = LoggerFactory.getLogger(ThematiqueRepositoryImpl::class.java)

    override fun getThematique(thematiqueId: String): Thematique? {
        val thematique = getThematiqueList().find { it.id == thematiqueId }

        if (thematique == null) logger.error("Thematique id '$thematiqueId' non trouvée")

        return thematique
    }

    override fun getThematiqueList() = when (val cacheResult = cacheRepository.getThematiqueList()) {
        is CacheListResult.CacheNotInitialized -> getThematiqueListAndCacheIt()
        is CacheListResult.CachedThematiqueList -> cacheResult.thematiqueList
    }

    internal fun getThematiqueListAndCacheIt(): List<Thematique> {
        val thematiques = if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiThematiques)) {
            strapiRepository.getThematiques().let(mapper::toDomain)
        } else {
            databaseRepository.getThematiqueList().map(mapper::toDomain)
        }

        if (thematiques.isNotEmpty()) {
            cacheRepository.insertThematiqueList(thematiques)
        }

        return thematiques
    }
}

package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component

@Component
class ThematiqueRepositoryImpl(
    private val cacheRepository: ThematiqueCacheRepository,
    private val databaseRepository: ThematiqueDatabaseRepository,
    private val mapper: ThematiqueMapper,
) : ThematiqueRepository {
    override fun getThematique(thematiqueId: String): Thematique? {
        return getThematiqueList().find { it.id == thematiqueId }
    }

    override fun getThematiqueList() = when (val cacheResult = cacheRepository.getThematiqueList()) {
        is CacheListResult.CacheNotInitialized -> getThematiqueListAndCacheIt()
        is CacheListResult.CachedThematiqueList -> cacheResult.thematiqueList
    }

    internal fun getThematiqueListAndCacheIt(): List<Thematique> {
        val thematiqueList = databaseRepository.getThematiqueList()
            .map(mapper::toDomain)
        cacheRepository.insertThematiqueList(thematiqueList)
        return thematiqueList
    }
}

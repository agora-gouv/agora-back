package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.responseQag.repository.ThematiqueStrapiRepository
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component

@Component
class ThematiqueRepositoryImpl(
    private val cacheRepository: ThematiqueCacheRepository,
    private val databaseRepository: ThematiqueDatabaseRepository,
    private val strapiRepository: ThematiqueStrapiRepository,
    private val mapper: ThematiqueMapper,
) : ThematiqueRepository {
    override fun getThematique(thematiqueId: String): Thematique? {
        return getThematiqueList().find { it.id == thematiqueId }
    }

    override fun getThematiqueList() = when (val cacheResult = cacheRepository.getThematiqueList()) {
        is CacheListResult.CacheNotInitialized -> getThematiqueListAndCacheIt()
        is CacheListResult.CachedThematiqueList -> cacheResult.thematiqueList
    }

    // TODO : Comment ne pas afficher les thématiques en double ?
    // TODO : Est-ce que l'app mobile récupère toutes les thématiques et fait des getById ?
    internal fun getThematiqueListAndCacheIt(): List<Thematique> {
        // todo sortir ce mapping
        val strapiThematiques = strapiRepository.getThematiques().let { strapiDTO ->
            strapiDTO.data.map { thematique ->
                Thematique(
                    thematique.id,
                    thematique.attributes.label,
                    thematique.attributes.pictogramme
                )
            }
        }

        val thematiqueList = databaseRepository.getThematiqueList()
            .map(mapper::toDomain)

        cacheRepository.insertThematiqueList(thematiqueList + strapiThematiques)

        return thematiqueList + strapiThematiques
    }
}

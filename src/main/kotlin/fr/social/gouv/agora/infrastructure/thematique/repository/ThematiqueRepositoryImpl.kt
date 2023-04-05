package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import fr.social.gouv.agora.infrastructure.thematique.repository.ThematiqueRepositoryImpl.Companion.CACHE_NAME
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = [CACHE_NAME])
class ThematiqueRepositoryImpl(
    private val databaseRepository: ThematiqueDatabaseRepository,
    private val thematiqueMapper: ThematiqueMapper,
    private val cacheManager: CacheManager,
) : ThematiqueRepository {

    companion object {
        const val CACHE_NAME = "thematiqueCache"
        internal const val CACHE_KEY = "thematiqueList"
    }

    override fun getThematiqueList(): List<Thematique> {
        val thematiqueList = getThematiqueListFromCache() ?: getThematiqueListFromDatabase()
        return thematiqueList.map { dto -> thematiqueMapper.toDomain(dto) }
    }

    private fun getCache() = cacheManager.getCache(CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getThematiqueListFromCache(): List<ThematiqueDTO>? {
        return getCache()?.get(CACHE_KEY, List::class.java) as? List<ThematiqueDTO>
    }

    internal fun getThematiqueListFromDatabase(): List<ThematiqueDTO> {
        val thematiqueList = databaseRepository.getThematiqueList()
        getCache()?.put(CACHE_KEY, thematiqueList)
        return thematiqueList
    }

}
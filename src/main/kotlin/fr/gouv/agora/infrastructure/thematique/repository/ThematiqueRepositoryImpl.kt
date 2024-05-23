package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ThematiqueRepositoryImpl(
    private val cacheRepository: ThematiqueCacheRepository,
    private val databaseRepository: ThematiqueDatabaseRepository,
    private val mapper: ThematiqueMapper,
) : ThematiqueRepository {

    override fun getThematiqueList(): List<Thematique> {
        return getThematiqueListDTO().map(mapper::toDomain)
    }

    override fun getThematique(thematiqueId: String): Thematique? {
        return try {
            val thematiqueUUID = UUID.fromString(thematiqueId)
            getThematiqueListDTO().find { dto -> dto.id == thematiqueUUID }?.let(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getThematiqueListDTO() = when (val cacheResult = cacheRepository.getThematiqueList()) {
        CacheListResult.CacheNotInitialized -> getThematiqueListDtoFromDatabase()
        is CacheListResult.CachedThematiqueList -> cacheResult.thematiqueListDTO
    }

    internal fun getThematiqueListDtoFromDatabase(): List<ThematiqueDTO> {
        val thematiqueListDTO = databaseRepository.getThematiqueList()
        cacheRepository.insertThematiqueList(thematiqueListDTO)
        return thematiqueListDTO
    }

}

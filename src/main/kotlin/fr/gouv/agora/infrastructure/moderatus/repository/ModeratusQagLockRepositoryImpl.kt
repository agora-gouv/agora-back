package fr.gouv.agora.infrastructure.moderatus.repository

import fr.gouv.agora.infrastructure.moderatus.repository.ModeratusQagLockCacheRepository.CacheResult
import fr.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ModeratusQagLockRepositoryImpl(
    private val databaseRepository: ModeratusQagLockDatabaseRepository,
    private val cacheRepository: ModeratusQagLockCacheRepository,
    private val mapper: ModeratusQagLockMapper,
) : ModeratusQagLockRepository {

    override fun getLockedQagIds(): List<String> {
        return when (val cacheResult = cacheRepository.getLockedQagIds()) {
            is CacheResult.CachedLockedQagIds -> cacheResult.lockedQagIds
            CacheResult.CacheNotInitialized -> {
                databaseRepository
                    .findAll()
                    .map { moderatusQagLockDTO -> mapper.toQagId(moderatusQagLockDTO) }
                    .also { lockedQagIds -> cacheRepository.addLockedQagIds(lockedQagIds) }
            }
        }
    }

    override fun addLockedIds(lockedQagIds: List<String>) {
        lockedQagIds
            .mapNotNull { lockedQagId -> mapper.toDto(lockedQagId) }
            .takeIf { dtoList -> dtoList.isNotEmpty() }
            ?.let { dtoList ->
                val savedDtoList = databaseRepository.saveAll(dtoList)
                cacheRepository.addLockedQagIds(savedDtoList.map { moderatusQagLockDTO ->
                    mapper.toQagId(moderatusQagLockDTO)
                })
            }
    }

    override fun removeLockedQagId(qagId: String) {
        try {
            val qagUUID = UUID.fromString(qagId)
            databaseRepository.deleteLockedQag(qagUUID)
            cacheRepository.removeLockedQagId(qagId)
        } catch (e: IllegalArgumentException) {
            // Do nothing
        }
    }

}

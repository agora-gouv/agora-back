package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.Profile
import fr.social.gouv.agora.domain.ProfileInserting
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.profile.repository.ProfileInsertionResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProfileRepositoryImpl(
    private val databaseRepository: ProfileDatabaseRepository,
    private val cacheRepository: ProfileCacheRepository,
    private val mapper: ProfileMapper,
) : ProfileRepository {
    companion object {
        private const val MAX_INSERT_RETRY_COUNT = 3
    }

    override fun getProfile(userId: String): Profile? {
        return try {
            val userUUID = UUID.fromString(userId)
            when (val cacheResult = cacheRepository.getProfile(userUUID)) {
                CacheResult.CacheNotInitialized -> getProfileFromDatabase(userUUID)
                CacheResult.CachedProfileNotFound -> null
                is CacheResult.CachedProfile -> cacheResult.profileDTO
            }?.let { profileDTO -> mapper.toDomain(profileDTO) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun insertProfile(profileInserting: ProfileInserting): ProfileInsertionResult {
        repeat(MAX_INSERT_RETRY_COUNT) {
            mapper.toDto(profileInserting)?.let { profileDTO ->
                if (!databaseRepository.existsById(profileDTO.id)) {
                    databaseRepository.save(profileDTO)
                    cacheRepository.insertProfile(userUUID = profileDTO.userId, profileDTO = profileDTO)
                    return ProfileInsertionResult.SUCCESS
                }
            }
        }
        return ProfileInsertionResult.FAILURE
    }

    private fun getProfileFromDatabase(userUUID: UUID): ProfileDTO? {
        val profileDTO = databaseRepository.getProfile(userUUID)
        cacheRepository.insertProfile(userUUID, profileDTO)
        return profileDTO
    }
}

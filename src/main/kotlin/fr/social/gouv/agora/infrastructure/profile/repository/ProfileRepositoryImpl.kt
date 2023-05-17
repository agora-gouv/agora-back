package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.Profile
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.profile.repository.DateDemandeRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProfileRepositoryImpl(
    private val databaseRepository: ProfileDatabaseRepository,
    private val cacheRepository: ProfileCacheRepository,
    private val dateDemandeRepository: DateDemandeRepository,
    private val mapper: ProfileMapper,
) : ProfileRepository {

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

    private fun getProfileFromDatabase(userUUID: UUID): ProfileDTO? {
        val profileDTO = databaseRepository.getProfile(userUUID)
        cacheRepository.insertProfile(userUUID, profileDTO)
        profileDTO?.let { dateDemandeRepository.deleteDate(userUUID) }
        return profileDTO
    }
}

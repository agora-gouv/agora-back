package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID_STRING
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProfileCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val PROFILE_CACHE_NAME = "profileCache"
    }

    sealed class CacheResult {
        data class CachedProfile(val profileDTO: ProfileDTO) : CacheResult()
        object CachedProfileNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getProfile(userUUID: UUID): CacheResult {
        val profileDTO = try {
            getCache()?.get(userUUID.toString(), ProfileDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (profileDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            NOT_FOUND_UUID_STRING -> CacheResult.CachedProfileNotFound
            else -> CacheResult.CachedProfile(profileDTO)
        }
    }

    fun insertProfile(userUUID: UUID, profileDTO: ProfileDTO?) {
        getCache()?.put(userUUID.toString(), profileDTO ?: createProfileNotFound())
    }

    private fun createProfileNotFound(): ProfileDTO {
        return ProfileDTO(
            id = NOT_FOUND_UUID,
            gender = "",
            yearOfBirth = 0,
            department = "",
            cityType = "",
            jobCategory = "",
            voteFrequency = "",
            publicMeetingFrequency = "",
            consultationFrequency = "",
            userId = NOT_FOUND_UUID,
        )
    }

    private fun getCache() = cacheManager.getCache(PROFILE_CACHE_NAME)
}
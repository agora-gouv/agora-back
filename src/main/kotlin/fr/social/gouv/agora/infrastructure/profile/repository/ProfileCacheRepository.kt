package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID_STRING
import org.springframework.cache.CacheManager
import org.springframework.cache.set
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class ProfileCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val PROFILE_CACHE_NAME = "profileCache"
    }

    sealed class CacheResult {
        data class CachedProfileNotFound(val cachedProfileDTO: CachedProfileDTO) : CacheResult()
        object CachedProfileFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getProfileStatus(userUUID: UUID): CacheResult {
        val cachedProfileDTO = try {
            getCache()?.get(userUUID.toString(), CachedProfileDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (cachedProfileDTO?.profileDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            NOT_FOUND_UUID_STRING -> CacheResult.CachedProfileNotFound(cachedProfileDTO)
            else -> CacheResult.CachedProfileFound
        }
    }

    fun insertProfile(userUUID: UUID, profileDTO: ProfileDTO?) {
        getCache()?.put(userUUID.toString(), CachedProfileDTO(LocalDate.now().toString(), profileDTO ?: createProfileNotFound()))
    }

    fun updateDemandeProfileDate(userUUID: UUID) {
        getCache()?.set(userUUID.toString(), CachedProfileDTO(LocalDate.now().toString(), createProfileNotFound()))
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

    //TODO ajout fonction pour supprimer entrée du cache d'un user qui rempli ses données de profil

    private fun getCache() = cacheManager.getCache(PROFILE_CACHE_NAME)


}
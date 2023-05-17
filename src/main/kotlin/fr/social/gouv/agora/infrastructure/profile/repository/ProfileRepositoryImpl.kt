package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository.CacheResult
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class ProfileRepositoryImpl(
    private val databaseRepository: ProfileDatabaseRepository,
    private val cacheRepository: ProfileCacheRepository,
) : ProfileRepository {
    companion object {
        private const val NOMBRE_JOUR_DEMANDE_INFO_PROFIL = 30
    }

    override fun askForDemographicInfo(userId: String?): Boolean {
        return try {
            var askForDemographicInfo = false
            userId?.let {
                val userUUID = UUID.fromString(userId)
                when (val cacheResult = cacheRepository.getProfileStatus(userUUID)) {
                    CacheResult.CacheNotInitialized -> {
                        if (getProfileFromDatabase(userUUID) == null)
                            askForDemographicInfo = true
                    }

                    CacheResult.CachedProfileFound -> {}
                    is CacheResult.CachedProfileNotFound -> {
                        val cachedDateString = cacheResult.cachedProfileDTO.dateDemande
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val cachedDateLocalDate = LocalDate.parse(cachedDateString, formatter)
                        if (LocalDate.now()
                                .isAfter(
                                    cachedDateLocalDate.plusDays(
                                        NOMBRE_JOUR_DEMANDE_INFO_PROFIL.toLong()
                                    )
                                )
                        ) {
                            askForDemographicInfo = true
                            cacheRepository.updateDemandeProfileDate(userUUID)
                        }
                    }
                }
            }
            askForDemographicInfo
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun getProfileFromDatabase(userUUID: UUID): ProfileDTO? {
        val profileDTO = databaseRepository.getProfile(userUUID)
        cacheRepository.insertProfile(userUUID, profileDTO)
        return profileDTO
    }
}

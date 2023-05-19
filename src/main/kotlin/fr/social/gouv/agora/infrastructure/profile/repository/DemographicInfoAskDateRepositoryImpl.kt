package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class DemographicInfoAskDateRepositoryImpl(
    private val cacheRepository: DemographicInfoAskDateCacheRepository,
) : DemographicInfoAskDateRepository {

    override fun getDate(userId: String): LocalDate? {
        return try {
            cacheRepository.getDate(UUID.fromString(userId))
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun insertDate(userId: String) {
        try {
            cacheRepository.insertDate(UUID.fromString(userId))
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }

    override fun updateDate(userId: String) {
        try {
            cacheRepository.updateDate(UUID.fromString(userId))
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }

    override fun deleteDate(userId: String) {
        try {
            cacheRepository.deleteDate(UUID.fromString(userId))
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }
}
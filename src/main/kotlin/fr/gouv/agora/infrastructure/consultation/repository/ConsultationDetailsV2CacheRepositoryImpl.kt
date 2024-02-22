package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationDetailsV2CacheRepositoryImpl(
    private val cacheManager: CacheManager,
    @Qualifier("shortTermCacheManager")
    private val shortTermCacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationDetailsV2CacheRepository {

    companion object {
        private const val CONSULTATION_DETAILS_CACHE_NAME = "consultationDetailsV2"
        private const val CONSULTATION_INFO_CACHE_NAME = "consultationDetailsInfoV2"

        private const val UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX = "unanswered/"
        private const val LATEST_CONSULTATION_DETAILS_PREFIX = "latest/"

        private const val PARTICIPANT_COUNT_PREFIX = "participantCount/"
        private const val HAS_ANSWERED_PREFIX = "hasAnswered/"
    }

    override fun getUnansweredUsersConsultationDetails(consultationId: String): ConsultationUpdateCacheResult {
        return try {
            when (val cachedValue =
                getCache()?.get("$UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX/$consultationId", String::class.java)) {
                null -> ConsultationUpdateCacheResult.CacheNotInitialized
                "" -> ConsultationUpdateCacheResult.ConsultationUpdateNotFound
                else -> ConsultationUpdateCacheResult.CachedConsultationsDetails(
                    details = objectMapper.readValue(cachedValue, ConsultationDetailsV2::class.java)
                )
            }
        } catch (e: Exception) {
            ConsultationUpdateCacheResult.CacheNotInitialized
        }
    }

    override fun initUnansweredUsersConsultationDetails(
        consultationId: String,
        consultationDetails: ConsultationDetailsV2?,
    ) {
        getCache()?.put(
            "$UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX/$consultationId",
            objectMapper.writeValueAsString(consultationDetails ?: ""),
        )
    }

    override fun getLastConsultationDetails(consultationId: String): ConsultationUpdateCacheResult {
        return try {
            when (val cachedValue =
                getCache()?.get("$LATEST_CONSULTATION_DETAILS_PREFIX/$consultationId", String::class.java)) {
                null -> ConsultationUpdateCacheResult.CacheNotInitialized
                "" -> ConsultationUpdateCacheResult.ConsultationUpdateNotFound
                else -> ConsultationUpdateCacheResult.CachedConsultationsDetails(
                    details = objectMapper.readValue(cachedValue, ConsultationDetailsV2::class.java)
                )
            }
        } catch (e: Exception) {
            ConsultationUpdateCacheResult.CacheNotInitialized
        }
    }

    override fun initLastConsultationDetails(consultationId: String, consultationDetails: ConsultationDetailsV2?) {
        getCache()?.put(
            "$LATEST_CONSULTATION_DETAILS_PREFIX/$consultationId",
            objectMapper.writeValueAsString(consultationDetails ?: ""),
        )
    }

    override fun getParticipantCount(consultationId: String): Int? {
        return try {
            return getShortTermCache()?.get("$PARTICIPANT_COUNT_PREFIX/$consultationId", Int::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun initParticipantCount(consultationId: String, participantCount: Int) {
        getShortTermCache()?.put("$PARTICIPANT_COUNT_PREFIX/$consultationId", participantCount)
    }

    override fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean? {
        return try {
            getCache()?.get("$HAS_ANSWERED_PREFIX/$consultationId/$userId", Boolean::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun initHasAnsweredConsultation(consultationId: String, userId: String, hasAnswered: Boolean) {
        getShortTermCache()?.put("$HAS_ANSWERED_PREFIX/$consultationId/$userId", hasAnswered)
    }

    override fun evictHasAnsweredConsultation(consultationId: String, userId: String) {
        getCache()?.evict("$HAS_ANSWERED_PREFIX/$consultationId/$userId")
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_DETAILS_CACHE_NAME)
    private fun getShortTermCache() = shortTermCacheManager.getCache(CONSULTATION_INFO_CACHE_NAME)

}
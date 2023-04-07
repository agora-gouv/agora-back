package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.consultation.repository.ConsultationRepositoryImpl.Companion.CACHE_NAME
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
@CacheConfig(cacheNames = [CACHE_NAME])
class ConsultationRepositoryImpl(
    private val databaseRepository: ConsultationDatabaseRepository,
    private val consultationMapper: ConsultationMapper,
    private val cacheManager: CacheManager,
) : ConsultationRepository {

    companion object {
        const val CACHE_NAME = "consultationCache"
        private const val CONSULTATION_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    override fun getConsultation(id: String): Consultation? {
        return try {
            val uuid = UUID.fromString(id)

            val cacheResult = getConsultationFromCache(uuid)
            when (cacheResult) {
                CacheResult.CacheNotInitialized -> getConsultationFromDatabase(uuid)
                CacheResult.CachedConsultationNotFound -> null
                is CacheResult.CachedConsultation -> cacheResult.consultationDTO
            }?.let { dto ->
                consultationMapper.toDomain(dto)
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getCache() = cacheManager.getCache(CACHE_NAME)

    private fun getConsultationFromCache(uuid: UUID): CacheResult {
        val cachedDto = getCache()?.get(uuid.toString(), ConsultationDTO::class.java)
        return when (cachedDto?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            CONSULTATION_NOT_FOUND_ID -> CacheResult.CachedConsultationNotFound
            else -> CacheResult.CachedConsultation(cachedDto)
        }
    }

    private fun getConsultationFromDatabase(uuid: UUID): ConsultationDTO? {
        val consulationDto = databaseRepository.getConsultation(uuid)
        getCache()?.put(uuid.toString(), consulationDto ?: createConsultationNotFound())
        return consulationDto
    }

    private fun createConsultationNotFound() = ConsultationDTO(
        id = UUID.fromString(CONSULTATION_NOT_FOUND_ID),
        title = "",
        abstract = "",
        start_date = null,
        end_date = Date(0),
        cover_url = "",
        question_count = "",
        estimated_time = "",
        participant_count_goal = 0,
        description = "",
        tips_description = "",
        id_thematique = UUID.fromString(CONSULTATION_NOT_FOUND_ID),
    )
}

private sealed class CacheResult {
    data class CachedConsultation(val consultationDTO: ConsultationDTO) : CacheResult()
    object CachedConsultationNotFound : CacheResult()
    object CacheNotInitialized : CacheResult()
}
package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsultationUpdateRepositoryImpl(
    private val consultationUpdateDatabaseRepository: ConsultationUpdateDatabaseRepository,
    private val consultationUpdatesCacheRepository: ConsultationUpdatesCacheRepository,
    private val explanationCacheRepository: ExplanationCacheRepository,
    private val explanationDatabaseRepository: ExplanationDatabaseRepository,
    private val mapper: ConsultationUpdateMapper,
) : ConsultationUpdateRepository {

    companion object {
        private const val CONSULTATION_UPDATE_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    override fun getConsultationUpdate(consultationId: String): ConsultationUpdate? {
        return try {
            val consultationUUID = UUID.fromString(consultationId)
            val cacheResult = consultationUpdatesCacheRepository.getConsultationUpdate(consultationUUID)

            when (cacheResult) {
                ConsultationUpdatesCacheRepository.CacheResult.CacheNotInitialized -> getConsultationUpdateFromDatabase(
                    consultationUUID
                )
                ConsultationUpdatesCacheRepository.CacheResult.CachedConsultationUpdateNotFound -> null
                is ConsultationUpdatesCacheRepository.CacheResult.CachedConsultationUpdate -> cacheResult.consultationUpdateDTO
            }?.let { consultationUpdateDTO ->
                val explanationList = getExplanations(consultationUpdateDTO.id)
                mapper.toDomain(consultationUpdateDTO, explanationList)
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getConsultationUpdateFromDatabase(consultationUUID: UUID): ConsultationUpdateDTO? {
        val consultationUpdateDTO = consultationUpdateDatabaseRepository.getLastConsultationUpdate(consultationUUID)
        val putDto = consultationUpdateDTO ?: createConsultationUpdateNotFound()
        consultationUpdatesCacheRepository.insertConsultationUpdate(consultationUUID, putDto)
        return consultationUpdateDTO
    }

    private fun getExplanations(consultationUpdateUUId: UUID): List<ExplanationDTO> {
        return when (val cacheResult = explanationCacheRepository.getExplanationList(consultationUpdateUUId)) {
            ExplanationCacheRepository.CacheResult.CacheNotInitialized -> getExplanationsFromDatabase(consultationUpdateUUId)
            is ExplanationCacheRepository.CacheResult.CachedExplanationList -> cacheResult.explanationDTOList
        }
    }

    private fun getExplanationsFromDatabase(consultationUpdateUUId: UUID): List<ExplanationDTO> {
        val explanationDTOList = explanationDatabaseRepository.getExplanationList(consultationUpdateUUId)
        explanationCacheRepository.insertExplanationList(consultationUpdateUUId, explanationDTOList)
        return explanationDTOList
    }


    private fun createConsultationUpdateNotFound() = ConsultationUpdateDTO(
        id = UUID.fromString(CONSULTATION_UPDATE_NOT_FOUND_ID),
        step = 0,
        description = "",
        consultationId = UUID.fromString(CONSULTATION_UPDATE_NOT_FOUND_ID),
        explanationsTitle = null,
        videoTitle = null,
        videoIntro = null,
        videoUrl = null,
        videoWidth = null,
        videoHeight = null,
        videoTranscription = null,
        conclusionTitle = null,
        conclusionDescription = null,
    )

}


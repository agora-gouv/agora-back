package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsultationUpdateRepositoryImpl(
    private val updateDatabaseRepository: ConsultationUpdateDatabaseRepository,
    private val updatesCacheRepository: ConsultationUpdateCacheRepository,
    private val explanationDatabaseRepository: ExplanationDatabaseRepository,
    private val explanationCacheRepository: ExplanationCacheRepository,
    private val mapper: ConsultationUpdateMapper,
) : ConsultationUpdateRepository {

    override fun getOngoingConsultationUpdate(consultationId: String): ConsultationUpdate? {
        return getConsultationUpdate(
            consultationId = consultationId,
            getFromCacheMethod = ConsultationUpdateCacheRepository::getOngoingConsultationUpdate,
            insertCacheMethod = ConsultationUpdateCacheRepository::insertOngoingConsultationUpdate,
            getFromDatabaseMethod = ConsultationUpdateDatabaseRepository::getOngoingConsultationUpdate,
        )
    }

    override fun getFinishedConsultationUpdate(consultationId: String): ConsultationUpdate? {
        return getConsultationUpdate(
            consultationId = consultationId,
            getFromCacheMethod = ConsultationUpdateCacheRepository::getFinishedConsultationUpdate,
            insertCacheMethod = ConsultationUpdateCacheRepository::insertFinishedConsultationUpdate,
            getFromDatabaseMethod = ConsultationUpdateDatabaseRepository::getFinishedConsultationUpdate,
        )
    }

    private fun getConsultationUpdate(
        consultationId: String,
        getFromCacheMethod: ConsultationUpdateCacheRepository.(UUID) -> ConsultationUpdateCacheRepository.CacheResult,
        insertCacheMethod: ConsultationUpdateCacheRepository.(UUID, ConsultationUpdateDTO) -> Unit,
        getFromDatabaseMethod: ConsultationUpdateDatabaseRepository.(UUID) -> ConsultationUpdateDTO?,
    ): ConsultationUpdate? {
        return try {
            getConsultationUpdateDTO(
                consultationUUID = UUID.fromString(consultationId),
                getFromCacheMethod = getFromCacheMethod,
                insertCacheMethod = insertCacheMethod,
                getFromDatabaseMethod = getFromDatabaseMethod,
            )?.let { consultationUpdateDTO ->
                val explanationList = getExplanations(consultationUpdateDTO.id)
                mapper.toDomain(consultationUpdateDTO, explanationList)
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getConsultationUpdateDTO(
        consultationUUID: UUID,
        getFromCacheMethod: ConsultationUpdateCacheRepository.(UUID) -> ConsultationUpdateCacheRepository.CacheResult,
        insertCacheMethod: ConsultationUpdateCacheRepository.(UUID, ConsultationUpdateDTO) -> Unit,
        getFromDatabaseMethod: ConsultationUpdateDatabaseRepository.(UUID) -> ConsultationUpdateDTO?,
    ): ConsultationUpdateDTO? {
        return when (val cacheResult = updatesCacheRepository.getFromCacheMethod(consultationUUID)) {
            is ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdate -> cacheResult.consultationUpdateDTO
            ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdateNotFound -> null
            ConsultationUpdateCacheRepository.CacheResult.CacheNotInitialized -> {
                val consultationUpdateDTO = updateDatabaseRepository.getFromDatabaseMethod(consultationUUID)
                updatesCacheRepository.insertCacheMethod(
                    consultationUUID,
                    consultationUpdateDTO ?: createConsultationUpdateNotFound()
                )
                consultationUpdateDTO
            }
        }
    }

    private fun getExplanations(consultationUpdateUUId: UUID): List<ExplanationDTO> {
        return when (val cacheResult = explanationCacheRepository.getExplanationList(consultationUpdateUUId)) {
            is ExplanationCacheRepository.CacheResult.CachedExplanationList -> cacheResult.explanationDTOList
            ExplanationCacheRepository.CacheResult.CacheNotInitialized -> {
                val explanationDTOList = explanationDatabaseRepository.getExplanationList(consultationUpdateUUId)
                explanationCacheRepository.insertExplanationList(consultationUpdateUUId, explanationDTOList)
                return explanationDTOList
            }
        }
    }

    private fun createConsultationUpdateNotFound() = ConsultationUpdateDTO(
        id = NOT_FOUND_UUID,
        step = 0,
        description = "",
        consultationId = NOT_FOUND_UUID,
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
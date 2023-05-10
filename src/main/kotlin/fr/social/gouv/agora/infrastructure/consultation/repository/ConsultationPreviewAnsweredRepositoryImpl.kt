package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewAnsweredInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.consultation.repository.ConsultationAnsweredCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ConsultationPreviewAnsweredRepositoryImpl(
    private val consultationDatabaseRepository: ConsultationDatabaseRepository,
    private val consultationAnsweredCacheRepository: ConsultationAnsweredCacheRepository,
    private val mapper: ConsultationPreviewAnsweredMapper,
) : ConsultationPreviewAnsweredRepository {

    override fun getConsultationAnsweredList(userId: String?): List<ConsultationPreviewAnsweredInfo> {
        return try {
            val userIdUUID = userId?.let { UUID.fromString(userId) }
            val consultationAnsweredDTOList =
                if (userIdUUID != null) {
                    when (val cacheResult =
                        consultationAnsweredCacheRepository.getConsultationAnsweredList(userIdUUID)) {
                        CacheResult.CacheNotInitialized -> getConsultationAnsweredListFromDatabase(userIdUUID)
                        is CacheResult.CachedConsultationAnsweredList -> cacheResult.consultationAnsweredListDTO
                    }
                } else {
                    emptyList()
                }
            consultationAnsweredDTOList.map { dto -> mapper.toDomain(dto) }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    override fun deleteConsultationAnsweredList(userId: String) {
        consultationAnsweredCacheRepository.deleteConsultationAnsweredList(UUID.fromString(userId))
    }

    private fun getConsultationAnsweredListFromDatabase(userId: UUID): List<ConsultationDTO> {
        val consultationAnsweredListDTO = consultationDatabaseRepository.getConsultationAnsweredList(userId)
        consultationAnsweredCacheRepository.insertConsultationAnsweredList(
            userId = userId,
            consultationAnsweredListDTO = consultationAnsweredListDTO
        )
        return consultationAnsweredListDTO
    }

}
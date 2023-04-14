package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.GetReponseConsultationRepositoryImpl.Companion.REPONSE_CONSULTATION_CACHE_NAME
import fr.social.gouv.agora.usecase.reponseConsultation.repository.ReponseConsultationRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = [REPONSE_CONSULTATION_CACHE_NAME])
class ReponseConsultationRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val reponseConsultationMapper: ReponseConsultationMapper,
    private val cacheManager: CacheManager,
) : ReponseConsultationRepository {

    override fun insertReponseConsultation(reponseConsultation: ReponseConsultationInserting): InsertStatus {
        val reponseConsultationDTOList = reponseConsultationMapper.toDto(reponseConsultation)
        for (reponseConsultationDTO in reponseConsultationDTOList) {
            if (!databaseRepository.existsById(reponseConsultationDTO.id))
                databaseRepository.save(reponseConsultationDTO)
            else
                return InsertStatus.INSERT_CONFLICT
        }
        cacheManager.getCache(REPONSE_CONSULTATION_CACHE_NAME)?.evict(reponseConsultation.consultationId)
        return InsertStatus.INSERT_SUCCESS
    }
}

enum class InsertStatus {
    INSERT_SUCCESS, INSERT_CONFLICT
}
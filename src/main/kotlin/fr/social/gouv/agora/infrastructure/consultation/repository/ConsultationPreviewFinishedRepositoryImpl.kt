package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewFinishedInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedRepositoryImpl(
    private val cacheRepository: ConsultationFinishedCacheRepository,
    private val databaseRepository: ConsultationDatabaseRepository,
    private val mapper: ConsultationPreviewFinishedInfoMapper,
) : ConsultationPreviewFinishedRepository {

    override fun getConsultationFinishedList(): List<ConsultationPreviewFinishedInfo> {
        return when (val cacheResult = cacheRepository.getConsultationFinishedList()) {
            ConsultationFinishedCacheRepository.CacheResult.CacheNotInitialized -> getConsultationFinishedFromDatabase()
            is ConsultationFinishedCacheRepository.CacheResult.CachedConsultationFinishedList -> cacheResult.consultationFinishedListDTO
        }.map(mapper::toDomain)
    }

    private fun getConsultationFinishedFromDatabase(): List<ConsultationDTO> {
        return databaseRepository.getConsultationFinishedList().also { consultationDTOList ->
            cacheRepository.insertConsultationFinishedList(consultationDTOList)
        }
    }

}
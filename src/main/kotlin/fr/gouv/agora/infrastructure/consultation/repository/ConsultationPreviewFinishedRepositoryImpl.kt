package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinishedInfo
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
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

    override fun clearCache() {
        cacheRepository.clearCache()
    }

    private fun getConsultationFinishedFromDatabase(): List<ConsultationDTO> {
        return databaseRepository.getConsultationFinishedList().also { consultationDTOList ->
            cacheRepository.insertConsultationFinishedList(consultationDTOList)
        }
    }

}
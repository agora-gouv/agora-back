package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationOngoingCacheRepository.CacheResult
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewOngoingRepositoryImpl(
    private val databaseRepository: ConsultationDatabaseRepository,
    private val cacheRepository: ConsultationOngoingCacheRepository,
    private val mapper: ConsultationPreviewOngoingInfoMapper,
) : ConsultationPreviewOngoingRepository {

    override fun getConsultationPreviewOngoingList(): List<ConsultationPreviewOngoingInfo> {
        val consultationOngoingDTOList =
            when (val cacheResult = cacheRepository.getConsultationOngoingList()) {
                CacheResult.CacheNotInitialized -> getConsultationOngoingListFromDatabase()
                is CacheResult.CachedConsultationOngoingList -> cacheResult.consultationOngoingListDTO
            }
        return consultationOngoingDTOList.map { dto -> mapper.toDomain(dto) }
    }

    override fun clearCache() {
        cacheRepository.clearCache()
    }

    private fun getConsultationOngoingListFromDatabase(): List<ConsultationDTO> {
        val consultationOngoingListDTO = databaseRepository.getConsultationOngoingList()
        cacheRepository.insertConsultationOngoingList(consultationOngoingListDTO)
        return consultationOngoingListDTO
    }

}
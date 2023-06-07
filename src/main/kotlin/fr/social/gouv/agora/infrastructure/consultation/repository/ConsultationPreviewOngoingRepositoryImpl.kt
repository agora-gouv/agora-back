package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.consultation.repository.ConsultationOngoingCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewOngoingRepositoryImpl(
    private val consultationDatabaseRepository: ConsultationDatabaseRepository,
    private val consultationOngoingCacheRepository: ConsultationOngoingCacheRepository,
    private val mapper: ConsultationPreviewOngoingMapper,
) : ConsultationPreviewOngoingRepository {

    override fun getConsultationPreviewOngoingList(): List<ConsultationPreviewOngoingInfo> {
        val consultationOngoingDTOList =
            when (val cacheResult = consultationOngoingCacheRepository.getConsultationOngoingList()) {
                CacheResult.CacheNotInitialized -> getConsultationOngoingListFromDatabase()
                is CacheResult.CachedConsultationOngoingList -> cacheResult.consultationOngoingListDTO
            }
        return consultationOngoingDTOList.map { dto -> mapper.toDomain(dto) }
    }

    private fun getConsultationOngoingListFromDatabase(): List<ConsultationDTO> {
        val consultationOngoingListDTO = consultationDatabaseRepository.getConsultationOngoingList()
        consultationOngoingCacheRepository.insertConsultationOngoingList(consultationOngoingListDTO)
        return consultationOngoingListDTO
    }

}
package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
//import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.stereotype.Component
import java.util.*
/*
@Component
class QagModeratingRepositoryImpl(
    private val cacheRepository: QagCacheRepository,
    private val mapper: QagInfoMapper,
) : QagInfoRepository {

    override fun getQagInfo(qagId: String): QagInfo? {
        return try {
            val qagUUID = UUID.fromString(qagId)

            when (val cacheResult = cacheRepository.getQag(qagUUID)) {
                CacheResult.CacheNotInitialized -> getQagFromDatabase(qagUUID)
                CacheResult.CachedQagNotFound -> null
                is CacheResult.CachedQag -> cacheResult.qagDTO
            }?.let { qagDTO -> mapper.toDomain(qagDTO) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult {
        val qagDTO = mapper.toDto(qagInserting)
        return if (qagDTO != null) {
            val savedQagDTO = databaseRepository.save(qagDTO)
            cacheRepository.insertQag(qagUUID = savedQagDTO.id, qagDTO = savedQagDTO)
            QagInsertionResult.SUCCESS
        } else QagInsertionResult.FAILURE
    }

    private fun getQagFromDatabase(qagUUID: UUID): QagDTO? {
        val qagDTO = databaseRepository.getQag(qagUUID)
        cacheRepository.insertQag(qagUUID, qagDTO)
        return qagDTO
    }*/
//}


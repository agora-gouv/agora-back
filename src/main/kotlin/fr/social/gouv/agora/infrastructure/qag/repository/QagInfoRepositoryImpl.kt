package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagInfoRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagCacheRepository,
    private val mapper: QagInfoMapper,
) : QagInfoRepository {
    companion object {
        private const val MAX_INSERT_RETRY_COUNT = 3
    }

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
        repeat(MAX_INSERT_RETRY_COUNT) {
            mapper.toDto(qagInserting)?.let { qagDTO ->
                if (!databaseRepository.existsById(qagDTO.id)) {
                    databaseRepository.save(qagDTO)
                    cacheRepository.insertQag(qagUUID = qagDTO.id, qagDTO = qagDTO)
                    return QagInsertionResult.SUCCESS
                }
            }
        }
        return QagInsertionResult.FAILURE
    }

    private fun getQagFromDatabase(qagUUID: UUID): QagDTO? {
        val qagDTO = databaseRepository.getQag(qagUUID)
        cacheRepository.insertQag(qagUUID, qagDTO)
        return qagDTO
    }
}


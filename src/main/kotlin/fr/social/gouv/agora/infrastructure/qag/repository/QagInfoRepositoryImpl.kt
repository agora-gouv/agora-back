package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.repository.QagInfoCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.qag.repository.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagInfoRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagInfoCacheRepository,
    private val mapper: QagInfoMapper,
) : QagInfoRepository {

    override fun getAllQagInfo(): List<QagInfo> {
        return getAllQagDTO().map(mapper::toDomain)
    }

    override fun getQagInfo(qagId: String): QagInfo? {
        return try {
            val qagUUID = UUID.fromString(qagId)
            getAllQagDTO()
                .find { qagDTO -> qagDTO.id == qagUUID }
                ?.let(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult {
        return mapper.toDto(qagInserting)?.let { qagDTO ->
            val savedQagDTO = databaseRepository.save(qagDTO)
            cacheRepository.insertQag(qagDTO = savedQagDTO)
            QagInsertionResult.Success(qagId = savedQagDTO.id)
        } ?: QagInsertionResult.Failure
    }

    override fun updateQagStatus(qagId: String, newQagStatus: QagStatus): QagUpdateResult {
        return try {
            val qagUUID = UUID.fromString(qagId)
            getAllQagDTO().find { qagDTO -> qagDTO.id == qagUUID }?.let { qagDTO ->
                val updatedQagDTO = mapper.updateQagStatus(dto = qagDTO, qagStatus = newQagStatus)
                val savedQagDTO = databaseRepository.save(updatedQagDTO)
                cacheRepository.updateQag(updatedQagDTO = savedQagDTO)
                QagUpdateResult.SUCCESS
            } ?: QagUpdateResult.FAILURE
        } catch (e: IllegalArgumentException) {
            QagUpdateResult.FAILURE
        }
    }

    override fun archiveQag(qagId: String): QagArchiveResult {
        return try {
            val qagUUID = UUID.fromString(qagId)
            getAllQagDTO().find { qagDTO -> qagDTO.id == qagUUID }?.let { qagDTO ->
                val archivedQagDTO = mapper.archiveQag(dto = qagDTO)
                databaseRepository.save(archivedQagDTO)
                cacheRepository.deleteQag(qagDTO)
                QagArchiveResult.SUCCESS
            } ?: QagArchiveResult.FAILURE
        } catch (e: IllegalArgumentException) {
            QagArchiveResult.FAILURE
        }
    }

    private fun getAllQagDTO() = when (val cacheResult = cacheRepository.getAllQagList()) {
        is CacheResult.CachedQagList -> cacheResult.allQagDTO
        CacheResult.CacheNotInitialized -> databaseRepository.getAllQagList().also { allQagDTO ->
            cacheRepository.initializeCache(allQagDTO)
        }
    }
}

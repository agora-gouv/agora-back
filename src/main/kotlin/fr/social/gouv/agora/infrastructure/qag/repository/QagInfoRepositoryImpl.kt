package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.repository.QagInfoCacheRepository.CacheResult
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
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

    override fun getQagInfoToModerateList(): List<QagInfo> {
        // TODO add cache & tests
        return databaseRepository.getQagToModerateList().map(mapper::toDomain)
    }

    override fun getDisplayedQagInfoList(thematiqueId: String?): List<QagInfo> {
        // TODO add tests
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getDisplayedQagList(thematiqueId = thematiqueUUID)
        } ?: databaseRepository.getDisplayedQagList()
        return qagList.map(mapper::toDomain)
    }

    override fun getUserQagInfoList(userId: String, thematiqueId: String?): List<QagInfo> {
        // TODO add cache & tests
        return userId.toUuidOrNull()?.let { userUUID ->
            val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
                databaseRepository.getUserQagList(userId = userUUID, thematiqueId = thematiqueUUID)
            } ?: databaseRepository.getUserQagList(userId = userUUID)
            qagList.map(mapper::toDomain)
        } ?: emptyList()
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
            try {
                cacheRepository.insertQag(qagDTO = savedQagDTO)
            } catch (e: IllegalStateException) {
                getAllQagFromDatabaseAndInitializeCache()
            }
            QagInsertionResult.Success(qagId = savedQagDTO.id)
        } ?: QagInsertionResult.Failure
    }

    override fun updateQagStatus(qagId: String, newQagStatus: QagStatus): QagUpdateResult {
        return try {
            val qagUUID = UUID.fromString(qagId)
            findQagDTO(qagUUID)?.let { qagDTO ->
                val updatedQagDTO = mapper.updateQagStatus(dto = qagDTO, qagStatus = newQagStatus)
                val savedQagDTO = databaseRepository.save(updatedQagDTO)
                try {
                    cacheRepository.updateQag(updatedQagDTO = savedQagDTO)
                } catch (e: IllegalStateException) {
                    getAllQagFromDatabaseAndInitializeCache()
                }
                QagUpdateResult.Success(mapper.toDomain(savedQagDTO))
            } ?: QagUpdateResult.Failure
        } catch (e: IllegalArgumentException) {
            QagUpdateResult.Failure
        }
    }

    override fun selectQagForResponse(qagId: String): QagUpdateResult {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.selectQagForResponse(qagUUID)
            cacheRepository.deleteQagList(listOf(qagUUID))
            QagUpdateResult.SUCCESS
        } ?: QagUpdateResult.FAILURE
    }

    override fun archiveOldQags(resetDate: Date) {
        databaseRepository.archiveQagsBeforeDate(resetDate)
        databaseRepository.anonymizeRejectedQagsBeforeDate(resetDate)
        cacheRepository.clear()
    }

    override fun deleteQag(qagId: String): QagDeleteResult {
        return try {
            val qagUUID = UUID.fromString(qagId)
            val resultDelete = databaseRepository.deleteQagById(qagUUID)
            if (resultDelete <= 0)
                QagDeleteResult.FAILURE
            else {
                try {
                    cacheRepository.deleteQagList(listOf(qagUUID))
                } catch (e: IllegalStateException) {
                    getAllQagFromDatabaseAndInitializeCache()
                }
                QagDeleteResult.SUCCESS
            }
        } catch (e: IllegalArgumentException) {
            QagDeleteResult.FAILURE
        }
    }

    private fun getAllQagDTO() = when (val cacheResult = cacheRepository.getAllQagList()) {
        is CacheResult.CachedQagList -> cacheResult.allQagDTO
        CacheResult.CacheNotInitialized -> getAllQagFromDatabaseAndInitializeCache()
    }

    private fun getAllQagFromDatabaseAndInitializeCache() = databaseRepository.getAllQagList().also { allQagDTO ->
        cacheRepository.initializeCache(allQagDTO)
    }

    private fun findQagDTO(qagUUID: UUID?) = getAllQagDTO().find { qagDTO -> qagDTO.id == qagUUID }
}




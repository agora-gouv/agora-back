package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.repository.QagInfoCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.qag.repository.ModeratingQagResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagInfoRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagInfoCacheRepository,
    private val mapper: QagInfoMapper,
) : QagInfoRepository {
    companion object {
        private const val STATUS_OPEN = 0
        private const val STATUS_MODERATED_REJECTED = -1
        private const val STATUS_MODERATED_ACCEPTED = 1
    }

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

    override fun updateQagStatus(qagId: String, qagModeratingStatus: QagStatus): ModeratingQagResult {
        return try {
            val qagUUID = UUID.fromString(qagId)
            val qagDTO = getAllQagDTO().find { qagDTO -> qagDTO.id == qagUUID && qagDTO.status == STATUS_OPEN }
            val targetStatusDTO = toDtoStatus(qagModeratingStatus)
            if (qagDTO != null && targetStatusDTO != null) {
                val targetDTO = qagDTO.copy(status = targetStatusDTO)
                val savedQagDTO = databaseRepository.save(targetDTO)
                cacheRepository.updateQag(qagDTOTarget = savedQagDTO)
                ModeratingQagResult.SUCCESS
            } else {
                ModeratingQagResult.FAILURE
            }
        } catch (e: IllegalArgumentException) {
            ModeratingQagResult.FAILURE
        }
    }

    private fun toDtoStatus(qagModeratingStatus: QagStatus): Int? {
        return when (qagModeratingStatus) {
            QagStatus.MODERATED_ACCEPTED -> STATUS_MODERATED_ACCEPTED
            QagStatus.MODERATED_REJECTED -> STATUS_MODERATED_REJECTED
            else -> null
        }
    }

    private fun getAllQagDTO() = when (val cacheResult = cacheRepository.getAllQagList()) {
        is CacheResult.CachedQagList -> cacheResult.allQagDTO
        CacheResult.CacheNotInitialized -> databaseRepository.getAllQagList().also { allQagDTO ->
            cacheRepository.initializeCache(allQagDTO)
        }
    }


}


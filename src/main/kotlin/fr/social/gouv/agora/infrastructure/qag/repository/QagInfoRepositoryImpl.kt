package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
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

    private fun getAllQagDTO() = if (cacheRepository.isInitialized()) {
        cacheRepository.getAllQagList()
    } else {
        databaseRepository.getAllQagList().also { allQagDTO ->
            cacheRepository.initializeCache(allQagDTO)
        }
    }

}


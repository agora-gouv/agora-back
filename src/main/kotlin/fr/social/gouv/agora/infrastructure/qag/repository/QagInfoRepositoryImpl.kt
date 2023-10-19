package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.social.gouv.agora.usecase.qag.repository.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagInfoRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val mapper: QagInfoMapper,
) : QagInfoRepository {

    override fun getQagInfoToModerateList(): List<QagInfo> {
        return databaseRepository.getQagToModerateList().map(mapper::toDomain)
    }

    override fun getPopularQags(thematiqueId: String?): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getPopularQags(thematiqueId = thematiqueUUID)
        } ?: databaseRepository.getPopularQags()
        return qagList.map(mapper::toDomain)
    }

    override fun getPopularQagsPaginated(
        thematiqueId: String?,
        maxDate: Date,
        pageNumber: Int
    ): List<QagInfoWithSupportCount> {
        TODO("Not yet implemented")
    }

    override fun getLatestQags(thematiqueId: String?): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getLatestQags(thematiqueId = thematiqueUUID)
        } ?: databaseRepository.getLatestQags()
        return qagList.map(mapper::toDomain)
    }

    override fun getLatestQagsPaginated(
        thematiqueId: String?,
        maxDate: Date,
        pageNumber: Int
    ): List<QagInfoWithSupportCount> {
        TODO("Not yet implemented")
    }

    override fun getSupportedQags(userId: String, thematiqueId: String?): List<QagInfoWithSupportCount> {
        val qagList = userId.toUuidOrNull()?.let { userUUID ->
            thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
                databaseRepository.getSupportedQags(userId = userUUID, thematiqueId = thematiqueUUID)
            } ?: databaseRepository.getSupportedQags(userId = userUUID)
        } ?: emptyList()
        return qagList.map(mapper::toDomain)
    }

    override fun getUserQagInfoList(userId: String, thematiqueId: String?): List<QagInfo> {
        return userId.toUuidOrNull()?.let { userUUID ->
            val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
                databaseRepository.getUserQagList(userId = userUUID, thematiqueId = thematiqueUUID)
            } ?: databaseRepository.getUserQagList(userId = userUUID)
            qagList.map(mapper::toDomain)
        } ?: emptyList()
    }

    override fun getQagInfo(qagId: String): QagInfo? {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getQagById(qagId = qagUUID)?.let { qagDTO ->
                mapper.toDomain(qagDTO)
            }
        }
    }

    override fun getQagWithSupportCount(qagId: String): QagInfoWithSupportCount? {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getQagWithSupportCount(qagId = qagUUID)?.let { qagDTO ->
                mapper.toDomain(qagDTO)
            }
        }
    }

    override fun getQagInfo(qagIds: List<String>): List<QagInfo> {
        TODO("Not yet implemented")
    }

    override fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult {
        return mapper.toDto(qagInserting)?.let { qagDTO ->
            val savedQagDTO = databaseRepository.save(qagDTO)
            QagInsertionResult.Success(qagInfo = mapper.toDomain(savedQagDTO))
        } ?: QagInsertionResult.Failure
    }

    override fun updateQagStatus(qagId: String, newQagStatus: QagStatus): QagUpdateResult {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            val updatedQagsCount = databaseRepository.updateQagStatus(
                qagId = qagUUID,
                newStatus = mapper.toIntStatus(newQagStatus),
            )
            if (updatedQagsCount <= 0) return QagUpdateResult.Failure
            when (val qagDTO = databaseRepository.getQagById(qagUUID)) {
                null -> QagUpdateResult.Failure
                else -> QagUpdateResult.Success(mapper.toDomain(qagDTO))
            }
        } ?: QagUpdateResult.Failure
    }

    override fun getMostPopularQags(): List<QagInfoWithSupportCount> {
        return databaseRepository.getMostPopularQags().map(mapper::toDomain)
    }

    override fun selectQagForResponse(qagId: String): QagUpdateResult {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            val updatedQagsCount = databaseRepository.selectQagForResponse(qagUUID)
            if (updatedQagsCount <= 0) return QagUpdateResult.Failure
            when (val qagDTO = databaseRepository.getQagById(qagUUID)) {
                null -> QagUpdateResult.Failure
                else -> QagUpdateResult.Success(mapper.toDomain(qagDTO))
            }
        } ?: QagUpdateResult.Failure
    }

    override fun archiveOldQags(resetDate: Date) {
        databaseRepository.archiveQagsBeforeDate(resetDate)
        databaseRepository.anonymizeRejectedQagsBeforeDate(resetDate)
    }

    override fun deleteQag(qagId: String): QagDeleteResult {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getQagById(qagUUID)?.let { qagDTO ->
                val resultDelete = databaseRepository.deleteQagById(qagUUID)
                if (resultDelete <= 0) return QagDeleteResult.Failure
                QagDeleteResult.Success(deletedQagInfo = mapper.toDomain(qagDTO))
            } ?: QagDeleteResult.Failure
        } ?: QagDeleteResult.Failure
    }

}




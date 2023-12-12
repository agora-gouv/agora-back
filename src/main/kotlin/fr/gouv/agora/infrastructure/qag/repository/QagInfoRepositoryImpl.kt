package fr.gouv.agora.infrastructure.qag.repository

import fr.gouv.agora.domain.QagInserting
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.qag.repository.*
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

    override fun getQagResponsesWithSupportCount(): List<QagInfoWithSupportCount> {
        return databaseRepository.getQagResponses().map(mapper::toDomain)
    }

    override fun getPopularQags(thematiqueId: String?): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getPopularQags(thematiqueId = thematiqueUUID)
        } ?: databaseRepository.getPopularQags()
        return qagList.map(mapper::toDomain)
    }

    override fun getLatestQags(thematiqueId: String?): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getLatestQags(thematiqueId = thematiqueUUID)
        } ?: databaseRepository.getLatestQags()
        return qagList.map(mapper::toDomain)
    }

    override fun getSupportedQags(userId: String, thematiqueId: String?): List<QagInfoWithSupportCount> {
        val qagList = userId.toUuidOrNull()?.let { userUUID ->
            thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
                databaseRepository.getSupportedQags(userId = userUUID, thematiqueId = thematiqueUUID)
            } ?: databaseRepository.getSupportedQags(userId = userUUID)
        } ?: emptyList()
        return qagList.map(mapper::toDomain)
    }

    override fun getUserLastQagInfo(userId: String): QagInfo? {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getLastUserQag(userId = userUUID)?.let(mapper::toDomain)
        }
    }

    override fun getPopularQagsPaginated(
        maxDate: Date,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getPopularQagsPaginated(
                maxDate = maxDate,
                offset = offset,
                thematiqueId = thematiqueUUID,
            )
        } ?: databaseRepository.getPopularQagsPaginated(
            maxDate = maxDate,
            offset = offset,
        )
        return qagList.map(mapper::toDomain)
    }

    override fun getLatestQagsPaginated(
        maxDate: Date,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getLatestQagsPaginated(
                maxDate = maxDate,
                offset = offset,
                thematiqueId = thematiqueUUID,
            )
        } ?: databaseRepository.getLatestQagsPaginated(
            maxDate = maxDate,
            offset = offset,
        )
        return qagList.map(mapper::toDomain)
    }

    override fun getSupportedQagsPaginated(
        userId: String,
        maxDate: Date,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount> {
        val qagList = userId.toUuidOrNull()?.let { userUUID ->
            thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
                databaseRepository.getSupportedQagsPaginated(
                    userId = userUUID,
                    maxDate = maxDate,
                    offset = offset,
                    thematiqueId = thematiqueUUID,
                )
            } ?: databaseRepository.getSupportedQagsPaginated(
                userId = userUUID,
                maxDate = maxDate,
                offset = offset,
            )
        } ?: emptyList()
        return qagList.map(mapper::toDomain)
    }

    override fun getPopularQagsPaginatedV2(
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getPopularQagsPaginatedV2(
                offset = offset,
                thematiqueId = thematiqueUUID,
            )
        } ?: databaseRepository.getPopularQagsPaginatedV2(
            offset = offset,
        )
        return qagList.map(mapper::toDomain)
    }

    override fun getLatestQagsPaginatedV2(
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount> {
        val qagList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getLatestQagsPaginatedV2(
                offset = offset,
                thematiqueId = thematiqueUUID,
            )
        } ?: databaseRepository.getLatestQagsPaginatedV2(
            offset = offset,
        )
        return qagList.map(mapper::toDomain)
    }

    override fun getSupportedQagsPaginatedV2(
        userId: String,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount> {
        val qagList = userId.toUuidOrNull()?.let { userUUID ->
            thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
                databaseRepository.getSupportedQagsPaginatedV2(
                    userId = userUUID,
                    offset = offset,
                    thematiqueId = thematiqueUUID,
                )
            } ?: databaseRepository.getSupportedQagsPaginatedV2(
                userId = userUUID,
                offset = offset,
            )
        } ?: emptyList()
        return qagList.map(mapper::toDomain)
    }

    override fun getQagsCount(): Int {
        return databaseRepository.getQagsCount()
    }

    override fun getQagsCountByThematique(thematiqueId: String): Int {
        return thematiqueId.toUuidOrNull()
            ?.let { thematiqueUUId -> databaseRepository.getQagsCountByThematique(thematiqueUUId) } ?: 0
    }

    override fun getQagInfo(qagId: String): QagInfo? {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getQagById(qagId = qagUUID)?.let { qagDTO ->
                mapper.toDomain(qagDTO)
            }
        }
    }

    override fun getQagsInfo(qagIds: List<String>): List<QagInfo> {
        return databaseRepository.getQagByIds(qagIds = qagIds.mapNotNull { it.toUuidOrNull() }).map(mapper::toDomain)
    }

    override fun getQagWithSupportCount(qagId: String): QagInfoWithSupportCount? {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getQagWithSupportCount(qagId = qagUUID)?.let(mapper::toDomain)
        }
    }

    override fun getQagsWithSupportCount(qagIds: List<String>): List<QagInfoWithSupportCount> {
        return databaseRepository.getQagsWithSupportCount(
            qagIds = qagIds.mapNotNull { it.toUuidOrNull() }
        ).map(mapper::toDomain)
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

    override fun getTrendingQags(): List<QagInfoWithSupportCount> {
        return databaseRepository.getTrendingQags().map(mapper::toDomain)
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

    override fun getQagByKeywordsList(keywords: List<String>): List<QagInfoWithSupportCount> {
        val keywordsArray = keywords.map { keyword -> "%$keyword%" }.toTypedArray()
        return databaseRepository.getQagByKeywordsList(keywordsArray).map(mapper::toDomain)
    }
}




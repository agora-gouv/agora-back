package fr.gouv.agora.usecase.qag.repository

import fr.gouv.agora.domain.QagInserting
import fr.gouv.agora.domain.QagStatus
import java.util.*
import kotlin.time.Duration

interface QagInfoRepository {
    fun getQagInfoToModerateList(): List<QagInfo>
    fun getUserLastQagInfo(userId: String): QagInfo?
    fun getQagsSelectedForResponse(): List<QagInfoWithSupportCount>

    fun getPopularQagsPaginatedV2(
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount>

    fun getLatestQagsPaginatedV2(
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount>

    fun getSupportedQagsPaginatedV2(
        userId: String,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount>

    fun getQagsCount(thematiqueId: String?): Int
    fun getQagInfo(qagId: String): QagInfo?
    fun getQagsInfo(qagIds: List<String>): List<QagInfo>
    fun getQagWithSupportCount(qagId: String): QagInfoWithSupportCount?
    fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult
    fun updateQagStatus(qagId: String, newQagStatus: QagStatus): QagUpdateResult
    fun getMostPopularQags(): List<QagInfoWithSupportCount>
    fun getTrendingQags(interval: Duration): List<QagInfoWithSupportCount>
    fun selectQagForResponse(qagId: String): QagUpdateResult
    fun archiveOldQags(resetDate: Date)
    fun deleteQag(qagId: String): QagDeleteResult
    fun getQagByKeywordsList(keywords: List<String>): List<QagInfoWithSupportCount>
    fun deleteUsersQag(userIDs: List<String>)
}

sealed class QagInsertionResult {
    data class Success(val qagInfo: QagInfo) : QagInsertionResult()
    object Failure : QagInsertionResult()
}

sealed class QagUpdateResult {
    data class Success(val updatedQagInfo: QagInfo) : QagUpdateResult()
    object Failure : QagUpdateResult()
}

sealed class QagDeleteResult {
    data class Success(val deletedQagInfo: QagInfo) : QagDeleteResult()
    object Failure : QagDeleteResult()
}

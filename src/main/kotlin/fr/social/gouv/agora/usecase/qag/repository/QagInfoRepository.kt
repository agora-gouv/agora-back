package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import java.util.*

interface QagInfoRepository {
    fun getQagInfoToModerateList(): List<QagInfo>
    fun getQagResponsesWithSupportCount(): List<QagInfoWithSupportCount>
    fun getPopularQags(thematiqueId: String?): List<QagInfoWithSupportCount>
    fun getLatestQags(thematiqueId: String?): List<QagInfoWithSupportCount>
    fun getSupportedQags(userId: String, thematiqueId: String?): List<QagInfoWithSupportCount>
    fun getUserQagInfoList(userId: String, thematiqueId: String?): List<QagInfo>
    fun getPopularQagsPaginated(
        maxDate: Date,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount>

    fun getLatestQagsPaginated(
        maxDate: Date,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount>

    fun getSupportedQagsPaginated(
        userId: String,
        maxDate: Date,
        offset: Int,
        thematiqueId: String?,
    ): List<QagInfoWithSupportCount>

    fun getQagsCount(): Int
    fun getQagInfo(qagId: String): QagInfo?
    fun getQagsInfo(qagIds: List<String>): List<QagInfo>
    fun getQagWithSupportCount(qagId: String): QagInfoWithSupportCount?
    fun getQagsWithSupportCount(qagIds: List<String>): List<QagInfoWithSupportCount>
    fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult
    fun updateQagStatus(qagId: String, newQagStatus: QagStatus): QagUpdateResult
    fun getMostPopularQags(): List<QagInfoWithSupportCount>
    fun selectQagForResponse(qagId: String): QagUpdateResult
    fun archiveOldQags(resetDate: Date)
    fun deleteQag(qagId: String): QagDeleteResult
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

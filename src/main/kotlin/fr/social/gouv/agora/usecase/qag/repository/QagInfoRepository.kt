package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import java.util.*

interface QagInfoRepository {
    fun getQagInfoToModerateList(): List<QagInfo>
    fun getQagResponsesWithSupportCount(): List<QagInfoWithSupportCount>
    fun getPopularQags(thematiqueId: String?): List<QagInfoWithSupportCount>
    fun getPopularQagsPaginated(thematiqueId: String?, maxDate: Date, pageNumber: Int): List<QagInfoWithSupportCount>
    fun getLatestQags(thematiqueId: String?): List<QagInfoWithSupportCount>
    fun getLatestQagsPaginated(thematiqueId: String?, maxDate: Date, pageNumber: Int): List<QagInfoWithSupportCount>
    fun getSupportedQags(userId: String, thematiqueId: String?): List<QagInfoWithSupportCount>
    fun getUserQagInfoList(userId: String, thematiqueId: String?): List<QagInfo>
    fun getQagInfo(qagId: String): QagInfo?
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

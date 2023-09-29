package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import java.util.*

interface QagInfoRepository {
    @Deprecated("TO DELETE")
    fun getAllQagInfo(): List<QagInfo>
    fun getQagInfoToModerateList(): List<QagInfo>
    fun getDisplayedQagInfoList(thematiqueId: String?): List<QagInfo>
    fun getUserQagInfoList(userId: String, thematiqueId: String?): List<QagInfo>

    // TODO: solo request
    fun getQagInfo(qagId: String): QagInfo?
    fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult
    fun updateQagStatus(qagId: String, newQagStatus: QagStatus): QagUpdateResult
    fun selectQagForResponse(qagId: String): QagUpdateResult
    fun archiveOldQags(resetDate: Date)

    @Deprecated("Remove")
    fun deleteQagListFromCache(qagIdList: List<String>): QagDeleteResult
    fun deleteQag(qagId: String): QagDeleteResult
}

sealed class QagInsertionResult {
    data class Success(val qagId: UUID) : QagInsertionResult()
    object Failure : QagInsertionResult()
}

sealed class QagUpdateResult {
    data class Success(val updatedQagInfo: QagInfo) : QagUpdateResult()
    object Failure : QagUpdateResult()
}

enum class QagDeleteResult {
    SUCCESS, FAILURE
}
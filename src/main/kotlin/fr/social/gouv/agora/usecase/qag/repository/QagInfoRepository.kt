package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import java.util.*

interface QagInfoRepository {
    fun getAllQagInfo(): List<QagInfo>
    fun getQagInfo(qagId: String): QagInfo?
    fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult
    fun updateQagStatus(qagId: String, newQagStatus: QagStatus): QagUpdateResult
}

sealed class QagInsertionResult {
    data class Success(val qagId: UUID) : QagInsertionResult()
    object Failure : QagInsertionResult()
}

enum class QagUpdateResult {
    SUCCESS, FAILURE
}

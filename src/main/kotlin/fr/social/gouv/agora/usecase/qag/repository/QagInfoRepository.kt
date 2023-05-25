package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import java.util.*

interface QagInfoRepository {
    fun getQagInfo(qagId: String): QagInfo?

    fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult
}
sealed class QagInsertionResult {
    data class Success(val qagId: UUID) : QagInsertionResult()
    object Failure : QagInsertionResult()
}
package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInserting

interface QagInfoRepository {
    fun getQagInfo(qagId: String): QagInfo?

    fun insertQagInfo(qagInserting: QagInserting): QagInsertionResult
}

enum class QagInsertionResult {
    SUCCESS, FAILURE
}
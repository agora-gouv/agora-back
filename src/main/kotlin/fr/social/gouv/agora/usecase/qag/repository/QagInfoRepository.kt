package fr.social.gouv.agora.usecase.qag.repository

interface QagInfoRepository {
    fun getQagInfo(qagId: String): QagInfo?

    fun insertQagInfo(qagInfo: QagInfo): QagInsertionResult
}

enum class QagInsertionResult {
    SUCCESS, FAILURE
}
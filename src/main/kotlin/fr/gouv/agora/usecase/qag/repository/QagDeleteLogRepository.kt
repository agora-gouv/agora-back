package fr.gouv.agora.usecase.qag.repository

import fr.gouv.agora.domain.QagDeleteLog

interface QagDeleteLogRepository {
    fun insertQagDeleteLog(qagDeleteLog: QagDeleteLog)
}


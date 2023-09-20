package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagDeleteLog

interface QagDeleteLogRepository {
    fun insertQagDeleteLog(qagDeleteLog: QagDeleteLog)
}


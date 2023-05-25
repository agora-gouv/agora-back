package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import java.util.*

interface QagInfoRepository {
    fun getQagInfo(qagId: String): QagInfo?

    fun insertQagInfo(qagInserting: QagInserting): UUID?
}


package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInfo

interface QagInfoRepository {
    fun getQagInfo(qagId: String): QagInfo?
}
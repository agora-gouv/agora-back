package fr.social.gouv.agora.usecase.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo

interface GetSupportQagRepository {
    fun getAllSupportQag(): List<SupportQagInfo>
    fun getSupportQag(qagId: String, userId: String): SupportQag?
}
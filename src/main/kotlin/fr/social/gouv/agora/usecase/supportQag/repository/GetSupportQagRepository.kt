package fr.social.gouv.agora.usecase.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo
import java.util.*

interface GetSupportQagRepository {
    fun getAllSupportQag(): List<SupportQagInfo>
    fun getSupportQag(qagId: String, userId: String): SupportQag?
    fun getQagSupportCounts(qagIds: List<String>): Map<String, Int>
    fun getUserSupportedQags(userId: String): List<String>
}
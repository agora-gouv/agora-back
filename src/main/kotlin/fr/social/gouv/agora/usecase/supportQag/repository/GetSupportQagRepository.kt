package fr.social.gouv.agora.usecase.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo
import java.util.*

interface GetSupportQagRepository {
    @Deprecated("should not be used anymore")
    fun getAllSupportQag(): List<SupportQagInfo>
    @Deprecated("should not be used anymore")
    fun getSupportQag(qagId: String, userId: String): SupportQag?
    fun getQagSupportCounts(qagIds: List<String>): Map<String, Int>
    fun getUserSupportedQags(userId: String): List<String>
}
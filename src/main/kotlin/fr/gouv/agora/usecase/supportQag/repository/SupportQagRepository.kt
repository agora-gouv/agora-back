package fr.gouv.agora.usecase.supportQag.repository

import fr.gouv.agora.domain.SupportQagDeleting
import fr.gouv.agora.domain.SupportQagInserting

interface SupportQagRepository {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult
    fun deleteSupportListByQagId(qagId: String): SupportQagResult
    fun deleteUsersSupportQag(userIDs: List<String>)
}

enum class SupportQagResult {
    SUCCESS, FAILURE
}
package fr.social.gouv.agora.usecase.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting

interface SupportQagRepository {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult
}

enum class SupportQagResult {
    SUCCESS, FAILURE
}
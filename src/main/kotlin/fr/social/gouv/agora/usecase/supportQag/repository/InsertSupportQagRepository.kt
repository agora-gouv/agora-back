package fr.social.gouv.agora.usecase.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagInserting

interface InsertSupportQagRepository {
    fun insertSupportQag(supportQag: SupportQagInserting): InsertSupportQagResult
}

enum class InsertSupportQagResult {
    SUCCESS, FAILURE
}
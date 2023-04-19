package fr.social.gouv.agora.usecase.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag

interface GetSupportQagRepository {
    fun getSupportQag(qagId: String, userId: String): SupportQag?
}
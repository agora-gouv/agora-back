package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.Qag

interface GetQagRepository {
    fun getQag(qagId: String): Qag?
}
package fr.social.gouv.agora.usecase.responseQag.repository

import fr.social.gouv.agora.domain.ResponseQag

interface ResponseQagListRepository {
    fun getResponseQagList(): List<ResponseQag>
}
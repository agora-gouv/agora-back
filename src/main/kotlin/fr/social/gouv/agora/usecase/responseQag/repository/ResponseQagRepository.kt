package fr.social.gouv.agora.usecase.responseQag.repository

import fr.social.gouv.agora.domain.ResponseQag

interface ResponseQagRepository {
    fun getResponsesQag(qagIds: List<String>): List<ResponseQag>
    fun getResponseQag(qagId: String): ResponseQag?
    fun getResponsesQagCount(): Int
    fun getResponsesQag(offset: Int): List<ResponseQag>
}
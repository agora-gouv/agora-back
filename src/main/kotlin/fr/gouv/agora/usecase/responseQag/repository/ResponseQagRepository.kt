package fr.gouv.agora.usecase.responseQag.repository

import fr.gouv.agora.domain.ResponseQag

interface ResponseQagRepository {
    fun getResponsesQag(qagIds: List<String>): List<ResponseQag>
    fun getResponseQag(qagId: String): ResponseQag?
    fun getResponsesQagCount(): Int
    fun getResponsesQag(from: Int, pageSize: Int = 20): List<ResponseQag>
}

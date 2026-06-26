package fr.gouv.agora.usecase.responseQag.repository

import fr.gouv.agora.domain.ResponseQag
import java.util.Date

interface ResponseQagRepository {
    fun getResponsesQag(qagIds: List<String>): List<ResponseQag>
    fun getResponseQag(qagId: String): ResponseQag?
    fun getResponsesQagCount(minDate: Date? = null): Int
    fun getResponsesQag(from: Int, pageSize: Int = 20, minDate: Date? = null): List<ResponseQag>
}

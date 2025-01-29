package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class ResponseQagRepositoryImpl(
    private val strapiRepository: ResponseQagStrapiRepository,
    private val mapper: ResponseQagMapper,
) : ResponseQagRepository {
    override fun getResponsesQag(qagIds: List<String>): List<ResponseQag> {
        val qagUUIDs = qagIds.mapNotNull { it.toUuidOrNull() }

        val strapiResponses = strapiRepository.getResponsesQag(qagUUIDs).let(mapper::toDomain)

        return strapiResponses
    }

    override fun getResponseQag(qagId: String): ResponseQag? {
        val qagUUID = qagId.toUuidOrNull() ?: return null

        return strapiRepository.getResponsesQag(listOf(qagUUID)).let(mapper::toDomain).firstOrNull()
    }

    override fun getResponsesQagCount(): Int {
        return strapiRepository.getResponsesCount()
    }

    override fun getResponsesQag(from: Int, pageSize: Int): List<ResponseQag> {
        val strapiResponses = strapiRepository.getResponsesQag().let(mapper::toDomain)

        val responsesQag = (strapiResponses).sortedByDescending { it.responseDate }
        val toIndex = min(responsesQag.size, from + pageSize)
        if (from > toIndex) return emptyList()

        return responsesQag.subList(from, toIndex)
    }
}

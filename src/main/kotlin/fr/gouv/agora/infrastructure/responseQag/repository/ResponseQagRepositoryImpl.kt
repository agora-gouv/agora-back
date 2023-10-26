package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ResponseQagRepositoryImpl(
    private val databaseRepository: ResponseQagDatabaseRepository,
    private val mapper: ResponseQagMapper,
) : ResponseQagRepository {

    override fun getResponsesQag(qagIds: List<String>): List<ResponseQag> {
        return databaseRepository.getResponsesQag(qagIds.mapNotNull { it.toUuidOrNull() }).map(mapper::toDomain)
    }

    override fun getResponseQag(qagId: String): ResponseQag? {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getResponseQag(qagId = qagUUID)?.let(mapper::toDomain)
        }
    }

    override fun getResponsesQagCount(): Int {
        return databaseRepository.getResponsesQagCount()
    }

    override fun getResponsesQag(offset: Int): List<ResponseQag> {
        return databaseRepository.getResponsesQag(offset = offset).map(mapper::toDomain)
    }
}
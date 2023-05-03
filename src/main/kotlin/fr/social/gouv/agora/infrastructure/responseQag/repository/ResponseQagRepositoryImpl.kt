package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.social.gouv.agora.infrastructure.responseQag.repository.ResponseQagCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ResponseQagRepositoryImpl(
    private val databaseRepository: ResponseQagDatabaseRepository,
    private val cacheRepository: ResponseQagCacheRepository,
    private val mapper: ResponseQagMapper,
) : ResponseQagRepository {

    override fun getResponseQag(qagId: String): ResponseQag? {
        return try {
            val qagUUID = UUID.fromString(qagId)

            when (val cacheResult = cacheRepository.getResponseQag(qagUUID)) {
                CacheResult.CacheNotInitialized -> getResponseQagFromDatabase(qagUUID)
                CacheResult.CachedResponseQagNotFound -> null
                is CacheResult.CachedResponseQag -> cacheResult.responseQagDTO
            }?.let(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getResponseQagFromDatabase(qagUUID: UUID): ResponseQagDTO? {
        return databaseRepository.getResponseQag(qagUUID).also { responseQagDTO ->
            cacheRepository.insertResponseQag(qagUUID, responseQagDTO)
        }
    }

}
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

    override fun getAllResponseQag(): List<ResponseQag> {
        return getAllResponseQagDTO().map(mapper::toDomain)
    }

    override fun getResponseQag(qagId: String): ResponseQag? {
        return try {
            val qagUUID = UUID.fromString(qagId)
            getAllResponseQagDTO()
                .find { responseQagDTO -> responseQagDTO.qagId == qagUUID }
                ?.let(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getAllResponseQagDTO(): List<ResponseQagDTO> {
        return when (val cacheResult = cacheRepository.getAllResponseQagList()) {
            is CacheResult.CachedResponseQagList -> cacheResult.allResponseQagDTO
            CacheResult.CacheNotInitialized -> databaseRepository.getAllResponseQagList()
                .also { allResponseQagDTO -> cacheRepository.initializeCache(allResponseQagDTO) }
        }
    }

}
package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.GetQagRepositoryImpl.Companion.QAG_CACHE_NAME
import fr.social.gouv.agora.usecase.qag.repository.GetQagRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
@CacheConfig(cacheNames = [QAG_CACHE_NAME])
class GetQagRepositoryImpl(
    private val databaseRepository: GetQagDatabaseRepository,
    private val mapper: QagMapper,
    private val cacheManager: CacheManager,
) : GetQagRepository {

    companion object {
        const val QAG_CACHE_NAME = "qagCache"
        private const val QAG_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    override fun getQag(qagId: String): Qag? {
        return try {
            val qagUUID = UUID.fromString(qagId)
            val cacheResult = getQagFromCache(qagUUID)
            when (cacheResult) {
                CacheResult.CacheNotInitialized -> getQagFromDatabase(qagUUID)
                CacheResult.CachedQagNotFound -> null
                is CacheResult.CachedQag -> cacheResult.qagDTO
            }?.let { qagDTO -> mapper.toDomain(qagDTO) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getCache() = cacheManager.getCache(QAG_CACHE_NAME)

    private fun getQagFromCache(qagUUID: UUID): CacheResult {
        val qagDTO = getCache()?.get(qagUUID.toString(), QagDTO::class.java)
        return when (qagDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            QAG_NOT_FOUND_ID -> CacheResult.CachedQagNotFound
            else -> CacheResult.CachedQag(qagDTO)
        }
    }

    private fun getQagFromDatabase(qagUUID: UUID): QagDTO? {
        val qagDTO = databaseRepository.getQag(qagUUID)
        getCache()?.put(qagUUID.toString(), qagDTO ?: createQagNotFound())
        return qagDTO
    }

    private fun createQagNotFound(): QagDTO {
        return QagDTO(
            id = UUID.fromString(QAG_NOT_FOUND_ID),
            title = "",
            description = "",
            postDate = Date(0),
            status = 0,
            username = "",
            thematiqueId = UUID.fromString(QAG_NOT_FOUND_ID),
        )
    }
}

private sealed class CacheResult {
    data class CachedQag(val qagDTO: QagDTO) : CacheResult()
    object CachedQagNotFound : CacheResult()
    object CacheNotInitialized : CacheResult()
}
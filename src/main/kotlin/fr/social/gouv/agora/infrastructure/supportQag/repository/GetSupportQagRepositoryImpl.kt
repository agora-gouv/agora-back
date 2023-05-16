package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetSupportQagRepositoryImpl(
    private val databaseRepository: SupportQagDatabaseRepository,
    private val cacheRepository: SupportQagCacheRepository,
    private val mapper: SupportQagMapper,
) : GetSupportQagRepository {

    override fun getSupportQag(qagId: String, userId: String): SupportQag? {
        return try {
            val qagUUID = UUID.fromString(qagId)
            val userUUID = UUID.fromString(userId)
            val supportQagDto = when (val cacheResult = cacheRepository.getSupportQag(qagUUID, userUUID)) {
                CacheResult.CacheNotInitialized -> getSupportQagFromDatabase(qagUUID = qagUUID, userUUID = userUUID)
                CacheResult.CachedSupportQagNotFound -> null
                is CacheResult.CachedSupportQag -> cacheResult.supportQagDTO
            }
            return mapper.toDomain(supportCount = getSupportCount(qagUUID), dto = supportQagDto)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getSupportCount(qagId: UUID): Int {
        return cacheRepository.getSupportCount(qagId) ?: getSupportCountFromDatabase(qagId)
    }

    private fun getSupportCountFromDatabase(qagUUID: UUID): Int {
        val supportCount = databaseRepository.getSupportCount(qagUUID)
        cacheRepository.insertSupportCount(qagUUID, supportCount)
        return supportCount
    }

    private fun getSupportQagFromDatabase(qagUUID: UUID, userUUID: UUID): SupportQagDTO? {
        val supportQagDTO = databaseRepository.getSupportQag(qagId = qagUUID, userId = userUUID)
        cacheRepository.insertSupportQag(qagId = qagUUID, userId = userUUID, supportQagDTO = supportQagDTO)
        return supportQagDTO
    }

}
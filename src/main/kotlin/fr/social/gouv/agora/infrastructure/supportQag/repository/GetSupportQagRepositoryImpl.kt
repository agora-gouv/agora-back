package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo
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

    override fun getAllSupportQag(): List<SupportQagInfo> {
        return getAllSupportQagDTO().map(mapper::toSupportQagInfo)
    }

    override fun getSupportQag(qagId: String, userId: String): SupportQag? {
        return try {
            val qagUUID = UUID.fromString(qagId)
            val userUUID = UUID.fromString(userId)

            val allSupportQagDTO = getAllSupportQagDTO()
            val qagSupportDTOList = allSupportQagDTO.filter { supportQagDTO -> supportQagDTO.qagId == qagUUID }
            val userSupportQagDTO = qagSupportDTOList.find { supportQagDTO -> supportQagDTO.userId == userUUID }
            return mapper.toDomain(supportCount = qagSupportDTOList.size, dto = userSupportQagDTO)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getAllSupportQagDTO() = when (val cacheResult = cacheRepository.getAllSupportQagList()) {
        is CacheResult.CachedSupportQagList -> cacheResult.allSupportQagDTO
        CacheResult.CacheNotInitialized -> databaseRepository.getAllSupportQagList().also { allSupportQagDTO ->
            cacheRepository.initializeCache(allSupportQagDTO = allSupportQagDTO)
        }
    }

}
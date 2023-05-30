package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
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

            val allSupportQagDTO = getAllSupportQagDTO()
            val qagSupportDTOList = allSupportQagDTO.filter { supportQagDTO -> supportQagDTO.qagId == qagUUID }
            val userSupportQagDTO = qagSupportDTOList.find { supportQagDTO -> supportQagDTO.userId == userUUID }
            return mapper.toDomain(supportCount = qagSupportDTOList.size, dto = userSupportQagDTO)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getAllSupportQagDTO() = if (cacheRepository.isInitialized()) {
        cacheRepository.getAllSupportQagList()
    } else {
        databaseRepository.getAllSupportQagList().also { allSupportQagDTO ->
            cacheRepository.initializeCache(allSupportQagDTO = allSupportQagDTO)
        }
    }

}
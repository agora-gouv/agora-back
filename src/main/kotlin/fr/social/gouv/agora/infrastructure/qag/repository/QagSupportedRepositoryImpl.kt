package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheSupportedListResult
import fr.social.gouv.agora.usecase.qag.repository.QagSupportedListRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagSupportedRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagCacheRepository,
    private val mapper: QagInfoMapper,
) : QagSupportedListRepository {

    override fun getQagSupportedList(thematiqueId: String?, userId: String?): List<QagInfo> {
        return try {
            val userIdUUID = userId?.let { UUID.fromString(userId) }
            val thematiqueUUID = thematiqueId?.let(UUID::fromString)
            if (userIdUUID != null) {
                val qagDTOList = when (val cacheResult =
                    cacheRepository.getQagSupportedList(thematiqueId = thematiqueUUID, userId = userIdUUID)) {
                    CacheSupportedListResult.CacheNotInitialized -> getQagSupportedListFromDatabase(
                        thematiqueUUID,
                        userIdUUID
                    )

                    is CacheSupportedListResult.CachedQagList -> cacheResult.qagListDTO
                }
                qagDTOList.map { dto -> mapper.toDomain(dto) }
            } else
                emptyList()
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    override fun deleteQagSupportedList(userId: String) {
        cacheRepository.deleteQagSupportedList(UUID.fromString(userId))
    }

    private fun getQagSupportedListFromDatabase(thematiqueUUID: UUID?, userIdUUID: UUID): List<QagDTO> {
        val qagDTO =
            thematiqueUUID?.let { databaseRepository.getQagSupportedListWithThematique(thematiqueUUID, userIdUUID) }
                ?: databaseRepository.getQagSupportedList(userIdUUID)
        cacheRepository.insertQagSupportedList(
            thematiqueId = thematiqueUUID,
            qagSupportedList = qagDTO,
            userId = userIdUUID
        )
        return qagDTO
    }
}


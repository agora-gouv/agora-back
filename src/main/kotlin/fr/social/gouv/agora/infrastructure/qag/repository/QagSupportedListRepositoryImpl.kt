package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheListResult
import fr.social.gouv.agora.usecase.qag.repository.QagSupportedListRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagSupportedListRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagCacheRepository,
    private val mapper: QagInfoMapper,
) : QagSupportedListRepository {

    override fun getQagSupportedList(thematiqueId: String?, userId: String): List<QagInfo> {
        return try {
            val userUUID = userId.let { UUID.fromString(userId) }
            val thematiqueUUID = thematiqueId?.let(UUID::fromString)
            if (userUUID != null) {
                val qagDTOList = when (val cacheResult =
                    cacheRepository.getQagSupportedList(thematiqueId = thematiqueUUID, userId = userUUID)) {
                    CacheListResult.CacheNotInitialized -> getQagSupportedListFromDatabase(
                        thematiqueUUID,
                        userUUID
                    )

                    is CacheListResult.CachedQagList -> cacheResult.qagListDTO
                }
                qagDTOList.map { dto -> mapper.toDomain(dto) }
            } else
                emptyList()
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    override fun deleteQagSupportedList(thematiqueId: String, userId: String) {
        cacheRepository.deleteQagSupportedList(UUID.fromString(thematiqueId), UUID.fromString(userId))
    }

    private fun getQagSupportedListFromDatabase(thematiqueUUID: UUID?, userUUID: UUID): List<QagDTO> {
        val qagDTO =
            thematiqueUUID?.let {
                databaseRepository.getQagSupportedListWithThematique(
                    thematiqueId = thematiqueUUID,
                    userId = userUUID
                )
            }
                ?: databaseRepository.getQagSupportedList(userUUID)
        cacheRepository.insertQagSupportedList(
            thematiqueId = thematiqueUUID,
            qagSupportedList = qagDTO,
            userId = userUUID
        )
        return qagDTO
    }
}


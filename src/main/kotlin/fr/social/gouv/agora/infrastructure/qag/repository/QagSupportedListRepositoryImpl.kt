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
            val userUUID = UUID.fromString(userId)
            val thematiqueUUID = thematiqueId?.let(UUID::fromString)
            val qagDTOList = when (val cacheResult =
                cacheRepository.getQagSupportedList(thematiqueId = thematiqueUUID, userId = userUUID)) {
                CacheListResult.CacheNotInitialized -> getQagSupportedListFromDatabase(
                    thematiqueUUID = thematiqueUUID,
                    userUUID = userUUID
                )

                is CacheListResult.CachedQagList -> cacheResult.qagListDTO
            }
            qagDTOList.map { dto -> mapper.toDomain(dto) }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    override fun deleteQagSupportedList(thematiqueId: String, userId: String) {
        try {
            val thematiqueUUID = UUID.fromString(thematiqueId)
            val userUUID = UUID.fromString(userId)
            cacheRepository.deleteQagSupportedList(thematiqueId = thematiqueUUID, userId = userUUID)
        }
        catch (e: IllegalArgumentException) {
            //do nothing
        }
    }

    private fun getQagSupportedListFromDatabase(thematiqueUUID: UUID?, userUUID: UUID): List<QagDTO> {
        val qagDTOList =
            thematiqueUUID?.let {
                databaseRepository.getQagSupportedListWithThematique(
                    thematiqueId = thematiqueUUID,
                    userId = userUUID
                )
            }
                ?: databaseRepository.getQagSupportedList(userUUID)
        cacheRepository.insertQagSupportedList(
            thematiqueId = thematiqueUUID,
            qagSupportedList = qagDTOList,
            userId = userUUID
        )
        return qagDTOList
    }
}


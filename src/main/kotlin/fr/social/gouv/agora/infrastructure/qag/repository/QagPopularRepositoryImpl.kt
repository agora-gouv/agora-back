package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CachePopularListResult
import fr.social.gouv.agora.usecase.qag.repository.QagPopularListRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagPopularRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagCacheRepository,
    private val mapper: QagInfoMapper,
) : QagPopularListRepository {

    override fun getQagPopularList(thematiqueId: String?): List<QagInfo> {
        return try {
            val thematiqueUUID = thematiqueId?.let(UUID::fromString)
            val qagDTOList = when (val cacheResult = cacheRepository.getQagPopularList(thematiqueUUID)) {
                CachePopularListResult.CacheNotInitialized -> getQagPopularListFromDatabase(thematiqueUUID)
                is CachePopularListResult.CachedQagList -> cacheResult.qagListDTO
            }
            qagDTOList.map { dto -> mapper.toDomain(dto) }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getQagPopularListFromDatabase(thematiqueUUID: UUID?): List<QagDTO> {
        val qagDTO = thematiqueUUID?.let { databaseRepository.getQagPopularListWithThematique(thematiqueUUID) }
            ?: databaseRepository.getQagPopularList()
        cacheRepository.insertQagPopularList(thematiqueUUID, qagDTO)
        return qagDTO
    }
}


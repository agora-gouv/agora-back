package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheListResult
import fr.social.gouv.agora.usecase.qag.repository.QagLatestListRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagLatestRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagCacheRepository,
    private val mapper: QagInfoMapper,
) : QagLatestListRepository {

    override fun getQagLatestList(thematiqueId: String?): List<QagInfo> {
        return try {
            val thematiqueUUID = thematiqueId?.let(UUID::fromString)
            val qagDTOList = when (val cacheResult = cacheRepository.getQagLatestList(thematiqueUUID)) {
                CacheListResult.CacheNotInitialized -> getQagLatestListFromDatabase(thematiqueUUID)
                is CacheListResult.CachedQagList -> cacheResult.qagListDTO
            }
            qagDTOList.map { dto -> mapper.toDomain(dto) }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getQagLatestListFromDatabase(thematiqueUUID: UUID?): List<QagDTO> {
        val qagDTO = thematiqueUUID?.let { databaseRepository.getQagLatestListWithThematique(thematiqueUUID) }
            ?: databaseRepository.getQagLatestList()
        cacheRepository.insertQagLatestList(thematiqueUUID, qagDTO)
        return qagDTO
    }
}


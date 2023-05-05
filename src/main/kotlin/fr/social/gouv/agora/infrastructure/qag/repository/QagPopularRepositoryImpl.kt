package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInfo
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CachePopularListResult
import fr.social.gouv.agora.usecase.qag.repository.QagPopularListRepository
import org.springframework.stereotype.Component

@Component
class QagPopularRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val cacheRepository: QagCacheRepository,
    private val mapper: QagInfoMapper,
) : QagPopularListRepository {

    override fun getQagPopularList(): List<QagInfo> {
        val qagDTOList =
            when (val cacheResult = cacheRepository.getQagPopularList()) {
                CachePopularListResult.CacheNotInitialized -> getQagPopularListFromDatabase()
                is CachePopularListResult.CachedQagList -> cacheResult.qagListDTO
            }
        return qagDTOList.map { dto -> mapper.toDomain(dto) }
    }

    private fun getQagPopularListFromDatabase(): List<QagDTO> {
        val qagDTO = databaseRepository.getQagPopularList()
        cacheRepository.insertQagPopularList(qagDTO)
        return qagDTO
    }
}


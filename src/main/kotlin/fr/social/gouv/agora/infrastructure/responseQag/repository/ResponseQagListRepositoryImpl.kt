package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.social.gouv.agora.infrastructure.responseQag.repository.ResponseQagCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagListRepository
import org.springframework.stereotype.Component

@Component
class ResponseQagListRepositoryImpl(
    private val databaseRepository: ResponseQagDatabaseRepository,
    private val cacheRepository: ResponseQagCacheRepository,
    private val mapper: ResponseQagMapper,
) : ResponseQagListRepository {

    override fun getResponseQagList(): List<ResponseQag> {
        val responseQagDTOList =
            when (val cacheResult = cacheRepository.getResponseQagList()) {
                is CacheResult.CachedResponseQagList -> cacheResult.responseQagListDTO
                else -> getResponseQagListFromDatabase()
            }
        return responseQagDTOList?.map { dto -> mapper.toDomain(dto) } ?: emptyList()
    }

    private fun getResponseQagListFromDatabase(): List<ResponseQagDTO>? {
        val responseQagListDTO = databaseRepository.getResponseQagList()
        cacheRepository.insertResponseQagList(responseQagListDTO)
        return responseQagListDTO
    }

}
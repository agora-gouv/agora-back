package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.*
import org.springframework.stereotype.Component

@Component
class QagModeratingRepositoryImpl(
    private val databaseRepository: QagInfoDatabaseRepository,
    private val mapper: QagModeratingInfoMapper,
) : QagModeratingListRepository {

    override fun getQagModeratingList(): List<QagModeratingInfo> {
        return try {
            val qagDTOList = databaseRepository.getQagModeratingList()
            qagDTOList.map { dto -> mapper.toDomain(dto) }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    override fun getModeratingQagCount(): Int {
        return databaseRepository.getModeratingQagCount()
    }
}


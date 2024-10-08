package fr.gouv.agora.infrastructure.qagUpdates.repository

import fr.gouv.agora.domain.QagInsertingUpdates
import fr.gouv.agora.domain.QagUpdates
import fr.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagUpdatesRepositoryImpl(
    private val databaseRepository: QagUpdatesDatabaseRepository,
    private val mapper: QagUpdatesMapper,
) : QagUpdatesRepository {

    override fun insertQagUpdates(qagInsertingUpdates: QagInsertingUpdates) {
        mapper.toDto(qagInsertingUpdates)?.let { qagUpdatesDTO -> databaseRepository.save(qagUpdatesDTO) }
    }

    override fun getQagUpdates(qagIdList: List<String>): List<QagUpdates> {
        return try {
            val qagUUIDList = qagIdList.map { qagId -> UUID.fromString(qagId) }
            databaseRepository.getQagUpdates(qagUUIDList).mapNotNull(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }
}

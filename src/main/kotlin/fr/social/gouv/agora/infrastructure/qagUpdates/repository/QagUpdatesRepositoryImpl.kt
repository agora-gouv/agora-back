package fr.social.gouv.agora.infrastructure.qagUpdates.repository

import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagUpdates
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
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

    override fun getQagUpdates(qagId: String): QagUpdates? {
        return try {
            val qagUUID = UUID.fromString(qagId)
            databaseRepository.getQagUpdates(qagUUID)?.let(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun gelAllQagUpdates(): List<QagUpdates> {
        return databaseRepository.findAll().map(mapper::toDomain)
    }
}

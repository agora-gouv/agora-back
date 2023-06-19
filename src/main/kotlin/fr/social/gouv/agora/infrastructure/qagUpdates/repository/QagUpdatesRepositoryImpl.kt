package fr.social.gouv.agora.infrastructure.qagUpdates.repository

import fr.social.gouv.agora.domain.QagUpdates
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Component

@Component
class QagUpdatesRepositoryImpl(
    private val databaseRepository: QagUpdatesDatabaseRepository,
    private val mapper: QagUpdatesMapper,
) : QagUpdatesRepository {

    override fun setQagUpdates(qagUpdates: QagUpdates) {
        mapper.toDto(qagUpdates)?.let { qagUpdatesDTO -> databaseRepository.save(qagUpdatesDTO) }
    }
}

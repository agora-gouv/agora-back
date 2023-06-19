package fr.social.gouv.agora.infrastructure.qagUpdates.repository

import fr.social.gouv.agora.infrastructure.qagUpdates.dto.QagUpdatesDTO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagUpdatesDatabaseRepository : CrudRepository<QagUpdatesDTO, UUID>

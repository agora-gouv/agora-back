package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDeleteLogDTO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagDeleteLogDatabaseRepository : CrudRepository<QagDeleteLogDTO, UUID>
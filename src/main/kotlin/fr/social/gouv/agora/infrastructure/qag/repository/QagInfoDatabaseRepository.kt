package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagInfoDatabaseRepository : CrudRepository<QagDTO, UUID> {

    @Query(value = "SELECT * FROM qags WHERE status <> 2", nativeQuery = true)
    fun getAllQagList(): List<QagDTO>
}

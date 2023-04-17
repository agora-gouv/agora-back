package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GetQagDatabaseRepository : CrudRepository<QagDTO, UUID> {
    @Query(value = "SELECT * FROM qags WHERE id = :qagId LIMIT 1", nativeQuery = true)
    fun getQag(@Param("qagId") qagId: UUID): QagDTO?
}
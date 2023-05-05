package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagInfoDatabaseRepository : CrudRepository<QagDTO, UUID> {
    @Query(value = "SELECT * FROM qags WHERE id = :qagId LIMIT 1", nativeQuery = true)
    fun getQag(@Param("qagId") qagId: UUID): QagDTO?
    @Query(value = """SELECT * FROM qags WHERE id IN 
        (SELECT qag_id FROM supports_qag GROUP BY qag_id ORDER BY COUNT(*) DESC LIMIT 10)""", nativeQuery = true)
    fun getQagPopularList(): List<QagDTO>
}
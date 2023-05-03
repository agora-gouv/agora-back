package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ResponseQagDatabaseRepository : CrudRepository<ResponseQagDTO, UUID> {

    @Query(value = "SELECT * FROM responses_qag WHERE qag_id = :qagId LIMIT 1", nativeQuery = true)
    fun getResponseQag(@Param("qagId") qagId: UUID): ResponseQagDTO?

    @Query(value = "SELECT * FROM responses_qag ORDER BY response_date DESC LIMIT 10", nativeQuery = true)
    fun getResponseQagList(): List<ResponseQagDTO>
}
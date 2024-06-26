package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ResponseQagDatabaseRepository : JpaRepository<ResponseQagDTO, UUID> {

    @Query(value = "SELECT * FROM responses_qag WHERE qag_id in :qagIds ORDER BY response_date DESC", nativeQuery = true)
    fun getResponsesQag(@Param("qagIds") qagIds: List<UUID>): List<ResponseQagDTO>

    @Query(
        value = "SELECT * FROM responses_qag WHERE qag_id = :qagId ORDER BY response_date DESC LIMIT 1",
        nativeQuery = true
    )
    fun getResponseQag(@Param("qagId") qagId: UUID): ResponseQagDTO?

    @Query(value = "SELECT count(*) FROM responses_qag", nativeQuery = true)
    fun getResponsesQagCount(): Int

    @Query(value = "SELECT * FROM responses_qag ORDER BY response_date", nativeQuery = true)
    fun getResponsesQag(): List<ResponseQagDTO>

}

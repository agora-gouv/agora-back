package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface QagInfoDatabaseRepository : CrudRepository<QagDTO, UUID> {

    @Query(value = """SELECT * FROM qags 
        WHERE status = 0 
        AND id NOT IN (SELECT qag_id FROM moderatus_locked_qags)
        SORT BY post_date ASC
        LIMIT 100""", nativeQuery = true)
    fun getQagToModerateList(): List<QagDTO>

    @Query(value = "SELECT * FROM qags WHERE status = 1 SORT BY post_date DESC", nativeQuery = true)
    fun getDisplayedQagList(): List<QagDTO>

    @Query(
        value = "SELECT * FROM qags WHERE status = 1 AND thematique_id = :thematiqueId SORT BY post_date DESC",
        nativeQuery = true,
    )
    fun getDisplayedQagList(@Param("thematiqueId") thematiqueId: UUID): List<QagDTO>

    @Query(value = "SELECT * FROM qags WHERE status = 0 AND user_id = :userId", nativeQuery = true)
    fun getUserQagList(@Param("userId") userId: UUID): List<QagDTO>

    @Query(
        value = "SELECT * FROM qags WHERE status = 0 AND user_id = :userId AND thematique_id = :thematiqueId",
        nativeQuery = true,
    )
    fun getUserQagList(@Param("userId") userId: UUID, @Param("thematiqueId") thematiqueId: UUID): List<QagDTO>

    @Query(value = "SELECT * from qags WHERE id = :qagId", nativeQuery = true)
    fun getQagById(@Param("qagId") qagId: UUID): QagDTO?

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM qags WHERE id = :qagId", nativeQuery = true)
    fun deleteQagById(@Param("qagId") qagId: UUID): Int

    @Modifying
    @Transactional
    @Query(value = "UPDATE qags SET status = :newStatus WHERE id = :qagId", nativeQuery = true)
    fun updateQagStatus(@Param("qagId") qagId: UUID, @Param("newStatus") newStatus: Int): Int

    @Modifying
    @Transactional
    @Query(
        value = """UPDATE qags 
        SET status = 7, user_id = '00000000-0000-0000-0000-000000000000'
        WHERE id = :qagId
        """, nativeQuery = true
    )
    fun selectQagForResponse(@Param("qagId") qagId: UUID): Int

    @Modifying
    @Transactional
    @Query(
        value = """UPDATE qags 
            SET status = 2, username = '', user_id = '00000000-0000-0000-0000-000000000000' 
            WHERE status = 1
            AND id IN (
                SELECT qag_id FROM qag_updates 
                WHERE status = 1 
                AND moderated_date < :resetDate 
            )""", nativeQuery = true
    )
    fun archiveQagsBeforeDate(@Param("resetDate") date: Date)

    @Modifying
    @Transactional
    @Query(
        value = """UPDATE qags 
            SET username = '', user_id = '00000000-0000-0000-0000-000000000000' 
            WHERE status = -1 
            AND id IN (
                SELECT qag_id FROM qag_updates 
                WHERE status = -1
                AND moderated_date < :resetDate
            )""", nativeQuery = true
    )
    fun anonymizeRejectedQagsBeforeDate(@Param("resetDate") date: Date)
}

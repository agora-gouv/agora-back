package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.dto.QagWithSupportCountDTO
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface QagInfoDatabaseRepository : CrudRepository<QagDTO, UUID> {

    companion object {
        private const val QAG_WITH_SUPPORT_COUNT_PROJECTION =
            "qags.id as id, title, description, post_date as postDate, status, username, thematique_id as thematiqueId, qags.user_id as userId, count(*) as supportCount"

        private const val QAG_WITH_SUPPORT_JOIN = "qags LEFT JOIN supports_qag ON qags.id = supports_qag.qag_id"
    }

    @Query(
        value = """SELECT * FROM qags 
        WHERE status = 0 
        AND id NOT IN (SELECT qag_id FROM moderatus_locked_qags)
        SORT BY post_date ASC
        LIMIT 100""", nativeQuery = true
    )
    fun getQagToModerateList(): List<QagDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 7
            GROUP BY qags.id
            ORDER BY postDate DESC
            LIMIT 5
        """, nativeQuery = true
    )
    fun getQagResponses(): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            GROUP BY qags.id
            ORDER BY supportCount DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getPopularQags(): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            AND thematique_id = :thematiqueId
            GROUP BY qags.id
            ORDER BY supportCount DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getPopularQags(@Param("thematiqueId") thematiqueId: UUID): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            GROUP BY qags.id
            ORDER BY postDate DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getLatestQags(): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            AND thematique_id = :thematiqueId
            GROUP BY qags.id
            ORDER BY postDate DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getLatestQags(@Param("thematiqueId") thematiqueId: UUID): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE (qags.status = 1 AND qags.id IN (SELECT qag_id FROM supports_qag WHERE user_id = :userId))
            OR ((qags.status = 0 OR qags.status = 1) AND qags.user_id = :userId)
            GROUP BY qags.id
            ORDER BY postDate DESC
            LIMIT 10;
        """, nativeQuery = true
    )
    fun getSupportedQags(@Param("userId") userId: UUID): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN 
            WHERE (
                (qags.status = 1 AND qags.id IN (SELECT qag_id FROM supports_qag WHERE user_id = :userId))
                OR ((qags.status = 0 OR qags.status = 1) AND qags.user_id = :userId)
            )
            AND thematique_id = :thematiqueId
            GROUP BY qags.id
            ORDER BY postDate DESC
            LIMIT 10;
        """, nativeQuery = true
    )
    fun getSupportedQags(
        @Param("userId") userId: UUID,
        @Param("thematiqueId") thematiqueId: UUID
    ): List<QagWithSupportCountDTO>

    @Query(value = "SELECT * FROM qags WHERE status = 0 AND user_id = :userId", nativeQuery = true)
    fun getUserQagList(@Param("userId") userId: UUID): List<QagDTO>

    @Query(
        value = "SELECT * FROM qags WHERE status = 0 AND user_id = :userId AND thematique_id = :thematiqueId",
        nativeQuery = true,
    )
    fun getUserQagList(@Param("userId") userId: UUID, @Param("thematiqueId") thematiqueId: UUID): List<QagDTO>

    @Query(value = "SELECT * from qags WHERE id = :qagId", nativeQuery = true)
    fun getQagById(@Param("qagId") qagId: UUID): QagDTO?

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN 
            WHERE qags.id = :qagId
            GROUP BY qags.id
    """, nativeQuery = true
    )
    fun getQagWithSupportCount(@Param("qagId") qagId: UUID): QagWithSupportCountDTO?

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN 
            WHERE qags.id IN :qagIds
            GROUP BY qags.id
    """, nativeQuery = true
    )
    fun getQagsWithSupportCount(@Param("qagIds") qagIds: List<UUID>): List<QagWithSupportCountDTO>

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM qags WHERE id = :qagId", nativeQuery = true)
    fun deleteQagById(@Param("qagId") qagId: UUID): Int

    @Modifying
    @Transactional
    @Query(value = "UPDATE qags SET status = :newStatus WHERE id = :qagId", nativeQuery = true)
    fun updateQagStatus(@Param("qagId") qagId: UUID, @Param("newStatus") newStatus: Int): Int

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION 
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE status = 1
            GROUP BY qags.id
            HAVING count(*) = (
                SELECT max(supportCount)
                FROM (
                    SELECT count(*) as supportCount
                    FROM $QAG_WITH_SUPPORT_JOIN
                    WHERE status = 1
                    GROUP BY qags.id
                ) as supportCounts
            )
        """, nativeQuery = true
    )
    fun getMostPopularQags(): List<QagWithSupportCountDTO>

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

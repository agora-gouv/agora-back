package fr.gouv.agora.infrastructure.qag.repository

import fr.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.gouv.agora.infrastructure.qag.dto.QagWithSupportCountDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface QagInfoDatabaseRepository : JpaRepository<QagDTO, UUID> {

    companion object {
        private const val QAG_WITH_SUPPORT_COUNT_PROJECTION =
            "qags.id as id, title, description, post_date as postDate, qags.status, username, thematique_id as thematiqueId, qags.user_id as userId, count(DISTINCT supports_qag.user_id) as supportCount"

        private const val QAG_WITH_SUPPORT_JOIN = "qags LEFT JOIN supports_qag ON qags.id = supports_qag.qag_id"
    }

    @Query(
        value = """SELECT * FROM qags 
            WHERE status = 0 
            AND id NOT IN (SELECT qag_id FROM moderatus_locked_qags)
            ORDER BY post_date ASC
            LIMIT 100
        """, nativeQuery = true
    )
    fun getQagToModerateList(): List<QagDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 7
            GROUP BY qags.id
            ORDER BY postDate DESC
            LIMIT 100
        """, nativeQuery = true
    )
    fun getLatestQagsSelectedForResponse(): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT * FROM qags 
        WHERE (status = 0 OR status = 1) 
        AND user_id = :userId 
        ORDER BY post_date DESC
        LIMIT 1
        """, nativeQuery = true
    )
    fun getLastUserQag(@Param("userId") userId: UUID): QagDTO?

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            GROUP BY qags.id
            ORDER BY supportCount DESC
            LIMIT 20
            OFFSET :offset
        """, nativeQuery = true
    )
    fun getPopularQagsPaginatedV2(
        @Param("offset") offset: Int,
    ): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            AND thematique_id = :thematiqueId
            GROUP BY qags.id
            ORDER BY supportCount DESC
            LIMIT 20
            OFFSET :offset
        """, nativeQuery = true
    )
    fun getPopularQagsPaginatedV2(
        @Param("offset") offset: Int,
        @Param("thematiqueId") thematiqueId: String,
    ): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            GROUP BY qags.id
            ORDER BY post_date DESC
            LIMIT 20
            OFFSET :offset
        """, nativeQuery = true
    )
    fun getLatestQagsPaginatedV2(
        @Param("offset") offset: Int,
    ): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE qags.status = 1
            AND thematique_id = :thematiqueId
            GROUP BY qags.id
            ORDER BY post_date DESC
            LIMIT 20
            OFFSET :offset
        """, nativeQuery = true
    )
    fun getLatestQagsPaginatedV2(
        @Param("offset") offset: Int,
        @Param("thematiqueId") thematiqueId: String,
    ): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN
            WHERE (
                (qags.status = 1 AND qags.id IN (SELECT qag_id FROM supports_qag WHERE user_id = :userId))
                OR ((qags.status = 0 OR qags.status = 1) AND qags.user_id = :userId)
            )
            GROUP BY qags.id
            ORDER BY CASE WHEN qags.user_id = :userId THEN 0 ELSE 1 END, (SELECT support_date FROM supports_qag WHERE qag_id = qags.id AND user_id = :userId LIMIT 1) DESC
            LIMIT 20
            OFFSET :offset
        """, nativeQuery = true
    )
    fun getSupportedQagsPaginatedV2(
        @Param("userId") userId: UUID,
        @Param("offset") offset: Int,
    ): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION
            FROM $QAG_WITH_SUPPORT_JOIN 
            WHERE (
                (qags.status = 1 AND qags.id IN (SELECT qag_id FROM supports_qag WHERE user_id = :userId))
                OR ((qags.status = 0 OR qags.status = 1) AND qags.user_id = :userId)
            )
            AND thematique_id = :thematiqueId
            GROUP BY qags.id
            ORDER BY CASE WHEN qags.user_id = :userId THEN 0 ELSE 1 END, (SELECT support_date FROM supports_qag WHERE qag_id = qags.id AND user_id = :userId LIMIT 1) DESC
            LIMIT 20
            OFFSET :offset
        """, nativeQuery = true
    )
    fun getSupportedQagsPaginatedV2(
        @Param("userId") userId: UUID,
        @Param("offset") offset: Int,
        @Param("thematiqueId") thematiqueId: String,
    ): List<QagWithSupportCountDTO>

    @Query(value = "SELECT count(*) FROM qags WHERE status = 1", nativeQuery = true)
    fun getQagsCount(): Int

    @Query(value = "SELECT count(*) FROM qags WHERE status = 1 AND thematique_id = :thematiqueId", nativeQuery = true)
    fun getQagsCountByThematique(@Param("thematiqueId") thematiqueId: String): Int

    @Query(value = "SELECT * from qags WHERE id = :qagId", nativeQuery = true)
    fun getQagById(@Param("qagId") qagId: UUID): QagDTO?

    @Query(value = "SELECT * from qags WHERE id IN :qagIds", nativeQuery = true)
    fun getQagByIds(@Param("qagIds") qagIds: List<UUID>): List<QagDTO>

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
        WHERE qags.status = 1
        AND (unaccent(title) ILIKE ALL (array[:keywords]) OR unaccent(description) ILIKE ALL (array[:keywords]))
        GROUP BY (qags.id)
        ORDER BY supportCount DESC
        LIMIT 20
    """,
        nativeQuery = true
    )
    fun getQagByKeywordsList(@Param("keywords") keywords: Array<String>): List<QagWithSupportCountDTO>

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

    @Query(
        value = """
            (
                SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION, 1 as columnOrder
                FROM $QAG_WITH_SUPPORT_JOIN
                WHERE qags.status = 1
                GROUP BY qags.id
                ORDER BY post_date DESC
                LIMIT 1
            )
            UNION
            (
                SELECT id, title, description, postDate, status, username, thematiqueId, userId, supportCount, 2 as columnOrder
                FROM (
                    SELECT $QAG_WITH_SUPPORT_COUNT_PROJECTION, ROW_NUMBER() OVER (PARTITION BY thematique_id ORDER BY count(*) DESC) as thematiqueRowNumber
                    FROM (qags LEFT JOIN qag_updates ON qags.id = qag_updates.qag_id) LEFT JOIN supports_qag ON qags.id = supports_qag.qag_id
                    WHERE qags.status = 1
                    AND qag_updates.status = 1
                    AND moderated_date >= (CURRENT_TIMESTAMP - INTERVAL '72 HOUR')
                    GROUP BY qags.id
                    ORDER BY supportCount DESC
                ) as rowNumber
                WHERE thematiqueRowNumber < 3
                LIMIT 20
            )
            ORDER BY columnOrder ASC, supportCount DESC
        """, nativeQuery = true
    )
    fun getTrendingQags(): List<QagWithSupportCountDTO>

    @Modifying
    @Transactional
    @Query(
        value = """UPDATE qags 
        SET status = 7
        WHERE id = :qagId
        """, nativeQuery = true
    )
    fun selectQagForResponse(@Param("qagId") qagId: UUID): Int

    @Modifying
    @Transactional
    @Query(
        value = """UPDATE qags 
            SET status = 2, username = ''
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

    @Modifying
    @Transactional
    @Query(
        value = """DELETE FROM qags 
            WHERE user_id IN :userIDs
            AND status <> 7""", nativeQuery = true
    )
    fun deleteUsersQags(@Param("userIDs") userIDs: List<UUID>)
}

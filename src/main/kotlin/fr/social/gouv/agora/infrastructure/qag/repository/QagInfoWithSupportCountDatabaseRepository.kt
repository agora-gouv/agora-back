package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagWithSupportCountDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagInfoWithSupportCountDatabaseRepository : JpaRepository<QagWithSupportCountDTO, UUID> {

    @Query(
        value = """SELECT qags.id as id, title, description, post_date, status, username, thematique_id, qags.user_id as user_id, count(*) as support_count
            FROM qags JOIN supports_qag 
            ON qags.id = supports_qag.qag_id 
            WHERE qags.status = 1
            GROUP BY (qags.id)
            ORDER BY support_count DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getPopularQags(): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT qags.id as id, title, description, post_date, status, username, thematique_id, qags.user_id as user_id, count(*) as support_count
            FROM qags JOIN supports_qag 
            ON qags.id = supports_qag.qag_id 
            WHERE qags.status = 1
            AND thematique_id = :thematiqueId
            GROUP BY (qags.id)
            ORDER BY support_count DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getPopularQags(@Param("thematiqueId") thematiqueId: UUID): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT qags.id, title, description, post_date, status, username, thematique_id, qags.user_id, count(*) as support_count
            FROM qags JOIN supports_qag 
            ON qags.id = supports_qag.qag_id 
            WHERE qags.status = 1
            OR (qags.status = 0 AND qags.user_id = :userId)
            GROUP BY (qags.id)
            ORDER BY post_date DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getLatestQags(@Param("userId") userId: UUID): List<QagWithSupportCountDTO>

    @Query(
        value = """SELECT qags.id, title, description, post_date, status, username, thematique_id, qags.user_id, count(*) as support_count
            FROM qags JOIN supports_qag 
            ON qags.id = supports_qag.qag_id 
            WHERE qags.status = 1
            OR (qags.status = 0 AND qags.user_id = :userId)
            AND thematique_id = :thematiqueId
            GROUP BY (qags.id)
            ORDER BY post_date DESC
            LIMIT 10
        """, nativeQuery = true
    )
    fun getLatestQags(
        @Param("userId") userId: UUID,
        @Param("thematiqueId") thematiqueId: UUID
    ): List<QagWithSupportCountDTO>

}

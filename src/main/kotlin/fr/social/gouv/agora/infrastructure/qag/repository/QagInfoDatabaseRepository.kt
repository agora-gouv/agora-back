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

    @Query(
        value = """
SELECT * FROM qags 
LEFT JOIN (SELECT count(*) as support_count, qag_id from supports_qag GROUP BY qag_id) as support_count_table 
ON qags.id = support_count_table.qag_id
WHERE id NOT IN (SELECT qag_id from responses_qag)
ORDER BY support_count
LIMIT 10
    """, nativeQuery = true
    )
    fun getQagPopularList(): List<QagDTO>

    @Query(
        value = """
SELECT * FROM qags 
LEFT JOIN (SELECT count(*) as support_count, qag_id from supports_qag GROUP BY qag_id) as support_count_table 
ON qags.id = support_count_table.qag_id
WHERE id NOT IN (SELECT qag_id from responses_qag)
AND thematique_id = :thematiqueId
ORDER BY support_count
LIMIT 10
""", nativeQuery = true
    )
    fun getQagPopularListWithThematique(@Param("thematiqueId") thematiqueId: UUID): List<QagDTO>

    @Query(value = "SELECT * FROM qags ORDER BY post_date DESC LIMIT 10", nativeQuery = true)
    fun getQagLatestList(): List<QagDTO>

    @Query(
        value = "SELECT * FROM qags where thematique_id = :thematiqueId ORDER BY post_date DESC LIMIT 10",
        nativeQuery = true
    )
    fun getQagLatestListWithThematique(@Param("thematiqueId") thematiqueId: UUID): List<QagDTO>
}
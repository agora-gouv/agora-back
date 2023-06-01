package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagInfoDatabaseRepository : CrudRepository<QagDTO, UUID> {

    @Query(value = "SELECT * FROM qags", nativeQuery = true)
    fun getAllQagList(): List<QagDTO>

    @Query(
        value = """
            SELECT * FROM qags where status = 0 
            AND id NOT IN (SELECT qag_id from responses_qag)  
            ORDER BY post_date ASC LIMIT 10
        """,
        nativeQuery = true
    )
    fun getQagModeratingList(): List<QagDTO>

    @Query(
        value = "SELECT count(*) FROM qags where status = 0 AND id NOT IN (SELECT qag_id from responses_qag) ",
        nativeQuery = true
    )
    fun getModeratingQagCount(): Int
}

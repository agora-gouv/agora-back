package fr.gouv.agora.infrastructure.headerQag.repository

import fr.gouv.agora.infrastructure.headerQag.dto.HeaderQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HeaderQagDatabaseRepository : JpaRepository<HeaderQagDTO, UUID> {
    @Query(
        value = """SELECT * FROM headers WHERE type = :filterType 
                    AND (start_date < CURRENT_DATE AND (end_date > CURRENT_DATE OR end_date IS NULL))
                    ORDER BY start_date DESC
                    LIMIT 1 """,
        nativeQuery = true
    )
    fun getHeader(@Param("filterType") filterType: String): HeaderQagDTO?
}
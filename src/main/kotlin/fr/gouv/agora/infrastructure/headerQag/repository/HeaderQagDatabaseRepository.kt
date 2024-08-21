package fr.gouv.agora.infrastructure.headerQag.repository

import fr.gouv.agora.infrastructure.headerQag.dto.HeaderQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface HeaderQagDatabaseRepository : JpaRepository<HeaderQagDTO, UUID> {
    @Query(
        value = """SELECT * FROM headers WHERE type = :filterType 
                    AND (start_date < :now AND (end_date > :now OR end_date IS NULL))
                    ORDER BY start_date DESC
                    LIMIT 1 """,
        nativeQuery = true
    )
    fun getLastHeader(
        @Param("filterType") filterType: String,
        @Param("now") now: LocalDateTime,
    ): HeaderQagDTO?
}

package fr.gouv.agora.infrastructure.participationCharter.repository

import fr.gouv.agora.infrastructure.participationCharter.dto.ParticipationCharterDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface ParticipationCharterDatabaseRepository : JpaRepository<ParticipationCharterDTO, UUID> {

    @Query(
        value = "SELECT extra_text FROM participation_charter_texts WHERE :now >= start_date ORDER BY start_date DESC LIMIT 1",
        nativeQuery = true,
    )
    fun getLatestParticipationCharterText(@Param("now") now: LocalDateTime): String

}

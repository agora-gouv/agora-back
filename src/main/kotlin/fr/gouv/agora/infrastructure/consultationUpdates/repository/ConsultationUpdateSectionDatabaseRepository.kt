package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateSectionDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationUpdateSectionDatabaseRepository : JpaRepository<ConsultationUpdateSectionDTO, UUID> {

    @Query(
        value = """SELECT * FROM consultation_update_sections 
            WHERE consultation_update_id = :consultationUpdateId
            ORDER BY ordre""",
        nativeQuery = true
    )
    fun getConsultationUpdateSections(@Param("consultationUpdateId") consultationUpdateId: UUID): List<ConsultationUpdateSectionDTO>

}
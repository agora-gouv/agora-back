package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeedbackConsultationUpdateDatabaseRepository : JpaRepository<FeedbackConsultationUpdateDTO, UUID> {

    @Query(
        value = """SELECT is_positive FROM feedbacks_consultation_update
            WHERE user_id = :userId
            AND consultation_update_id = :consultationUpdateId
            LIMIT 1
        """, nativeQuery = true
    )
    fun getUserConsultationUpdateFeedback(
        @Param("userId") userId: UUID,
        @Param("consultationUpdateId") consultationUpdateId: UUID,
    ): List<Int>

}
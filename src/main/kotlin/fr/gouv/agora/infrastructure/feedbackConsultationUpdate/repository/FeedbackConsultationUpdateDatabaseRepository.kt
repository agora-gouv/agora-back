package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateDTO
import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateStatsDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface FeedbackConsultationUpdateDatabaseRepository : JpaRepository<FeedbackConsultationUpdateDTO, UUID> {

    @Query(
        value = """SELECT * FROM feedbacks_consultation_update
            WHERE consultation_update_id = :consultationUpdateId
            AND user_id = :userId
            LIMIT 1
        """, nativeQuery = true
    )
    fun getUserConsultationUpdateFeedback(
        @Param("consultationUpdateId") consultationUpdateId: UUID,
        @Param("userId") userId: UUID,
    ): FeedbackConsultationUpdateDTO?

    @Query(
        value = """SELECT is_positive as hasPositiveValue, COUNT(*) as responseCount FROM feedbacks_consultation_update
            WHERE consultation_update_id = :consultationUpdateId
            GROUP BY is_positive
        """, nativeQuery = true
    )
    fun getConsultationUpdateFeedbackStats(
        @Param("consultationUpdateId") consultationUpdateId: UUID,
    ): List<FeedbackConsultationUpdateStatsDTO>

    @Modifying
    @Transactional
    @Query(
        value = """DELETE  FROM feedbacks_consultation_update
            WHERE consultation_update_id = :consultationUpdateId
            AND user_id = :userId
        """, nativeQuery = true
    )
    fun deleteFeedback(
        @Param("consultationUpdateId") consultationUpdateId: UUID,
        @Param("userId") userId: UUID,
    )

}
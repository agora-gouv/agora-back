package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeedbackQagDatabaseRepository : JpaRepository<FeedbackQagDTO, UUID> {
    @Query(
        value = "SELECT * FROM feedbacks_qag WHERE qag_id = :qagId",
        nativeQuery = true
    )
    fun getFeedbackQagList(@Param("qagId") qagId: UUID): List<FeedbackQagDTO>

    @Query(
        value = "SELECT qag_id FROM feedbacks_qag WHERE user_id = :userId",
        nativeQuery = true
    )
    fun getUserFeedbackQagIds(@Param("userId") userId: UUID): List<UUID>
}
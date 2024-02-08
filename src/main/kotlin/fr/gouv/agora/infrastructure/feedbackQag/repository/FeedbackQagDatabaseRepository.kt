package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
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

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM feedbacks_qag WHERE user_id IN :userIDs", nativeQuery = true
    )
    fun deleteUsersFeedbackQag(@Param("userIDs") userIDs: List<UUID>)
}
package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface FeedbackQagDatabaseRepository : JpaRepository<FeedbackQagDTO, UUID> {

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM feedbacks_qag WHERE user_id IN :userIDs", nativeQuery = true
    )
    fun deleteUsersFeedbackQag(@Param("userIDs") userIDs: List<UUID>)

    @Query(
        value = "SELECT * FROM feedbacks_qag WHERE qag_id = :qagId and user_id = :userId",
        nativeQuery = true
    )
    fun getFeedbackForQagAndUser(@Param("qagId") qagId: String, @Param("userId") userId: UUID): List<FeedbackQagDTO>

    @Query(
        value = "SELECT * FROM feedbacks_qag WHERE qag_id = :qagId",
        nativeQuery = true
    )
    fun getFeedbackQagList(@Param("qagId") qagId: String): List<FeedbackQagDTO>
}

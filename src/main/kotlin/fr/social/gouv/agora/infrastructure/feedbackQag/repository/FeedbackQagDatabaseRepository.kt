package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeedbackQagDatabaseRepository : CrudRepository<FeedbackQagDTO, UUID> {
    @Query(
        value = "SELECT * FROM feedbacks_qag WHERE qag_id = :qagId",
        nativeQuery = true
    )
    fun getFeedbackQagList(@Param("qagId") qagId: UUID): List<FeedbackQagDTO>
}
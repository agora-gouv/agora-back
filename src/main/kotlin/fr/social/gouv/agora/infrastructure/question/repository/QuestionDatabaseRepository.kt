package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface QuestionDatabaseRepository : CrudRepository<QuestionDTO, UUID> {
    @Query(value = "SELECT * FROM questions where consultation_id = :consultationId ORDER BY ordre", nativeQuery = true)
    fun getQuestionConsultation(@Param("consultationId") consultationId: UUID): List<QuestionDTO>?
}

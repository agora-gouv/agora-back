package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface QuestionDatabaseRepository : CrudRepository<QuestionDTO, UUID> {
    @Query(value = "SELECT * FROM questions where id_consultation=:id_consultation", nativeQuery = true)
    fun getQuestionConsultation(@Param("id_consultation") id_consultation:UUID): List<QuestionDTO>?
}

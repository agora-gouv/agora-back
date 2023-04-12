package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface ChoixPossibleDatabaseRepository : CrudRepository<ChoixPossibleDTO, UUID> {
    @Query(value = "SELECT * FROM choixpossible where question_id = :questionId ORDER BY ordre", nativeQuery = true)
    fun getChoixPossibleQuestion(@Param("questionId") questionId: UUID): List<ChoixPossibleDTO>?
}

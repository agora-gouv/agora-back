package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.infrastructure.login.dto.UserDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LoginDatabaseRepository : JpaRepository<UserDTO, UUID> {

    @Query("SELECT * FROM agora_users WHERE id = :userId LIMIT 1", nativeQuery = true)
    fun getUserById(@Param("userId") userId: UUID): UserDTO?

    @Query(
        value = """SELECT * FROM agora_users 
        WHERE id NOT IN (
             SELECT user_id FROM user_answered_consultation 
             WHERE consultation_id = :consultationId
        )""",
        nativeQuery = true,
    )
    fun getUsersNotAnsweredConsultation(@Param("consultationId") consultationId: UUID): List<UserDTO>
}
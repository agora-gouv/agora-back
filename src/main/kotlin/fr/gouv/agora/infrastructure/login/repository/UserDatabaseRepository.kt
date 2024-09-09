package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.infrastructure.login.dto.UserDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserDatabaseRepository : JpaRepository<UserDTO, UUID> {

    @Query("SELECT * FROM agora_users WHERE id = :userId LIMIT 1", nativeQuery = true)
    fun getUserById(@Param("userId") userId: UUID): UserDTO?

    @Query(
        value = """
        WITH unique_fcm_token_users AS (
            SELECT
                *,
                ROW_NUMBER() OVER (PARTITION BY fcm_token ORDER BY created_date DESC) AS created_date_rank
            FROM agora_users
        )
        
        SELECT * FROM unique_fcm_token_users 
                WHERE created_date_rank = 1
                AND id NOT IN (
                     SELECT user_id FROM user_answered_consultation 
                     WHERE consultation_id = :consultationId
                )
        """,
        nativeQuery = true,
    )
    fun getUsersNotAnsweredConsultation(@Param("consultationId") consultationId: String): List<UserDTO>

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM agora_users WHERE id IN :userIDs", nativeQuery = true)
    fun deleteUsers(@Param("userIDs") userIDs: List<UUID>)

    @Modifying
    @Transactional
    @Query(
        value = """UPDATE agora_users
            SET authorization_level = :authorization_level
            WHERE id IN :usersId
            """,
        nativeQuery = true
    )
    fun updateAuthorizationLevel(@Param("usersId") usersId: List<String>, @Param("authorizationLevel") authorizationLevel: Int): Int
}

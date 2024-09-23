package fr.gouv.agora.infrastructure.profile.repository

import fr.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface ProfileDatabaseRepository : JpaRepository<ProfileDTO, UUID> {
    @Query(value = "SELECT * FROM users_profile WHERE user_id = :userUUId LIMIT 1", nativeQuery = true)
    fun getProfile(@Param("userUUId") userUUId: UUID): ProfileDTO?

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_profile WHERE user_id IN :userIDs", nativeQuery = true)
    fun deleteUsersProfile(@Param("userIDs") userIDs: List<UUID>)

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE users_profile 
            SET primaryDepartment = :primaryDepartments, secondaryDepartment = :secondaryDepartments 
            WHERE user_id = :userID
        """, nativeQuery = true)
    fun updateDepartments(
        @Param("userID") userID: UUID,
        @Param("primaryDepartments") primaryDepartments: String?,
        @Param("secondaryDepartments") secondaryDepartments: String?
    )
}

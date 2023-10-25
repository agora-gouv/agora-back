package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileDatabaseRepository : JpaRepository<ProfileDTO, UUID> {
    @Query(value = "SELECT * FROM users_profile WHERE user_id = :userUUId LIMIT 1", nativeQuery = true)
    fun getProfile(@Param("userUUId") userUUId: UUID): ProfileDTO?
}
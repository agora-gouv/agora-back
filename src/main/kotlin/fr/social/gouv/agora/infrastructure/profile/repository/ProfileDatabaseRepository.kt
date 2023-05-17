package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileDatabaseRepository : CrudRepository<ProfileDTO, UUID> {
    @Query(value = "SELECT * FROM users_profile WHERE user_id = :userId LIMIT 1", nativeQuery = true)
    fun getProfile(@Param("userId") userId: UUID): ProfileDTO?
}
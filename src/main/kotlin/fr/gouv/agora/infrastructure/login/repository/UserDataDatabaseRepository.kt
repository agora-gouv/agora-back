package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.infrastructure.login.dto.UserDataDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface UserDataDatabaseRepository : JpaRepository<UserDataDTO, UUID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_data WHERE user_id IN :userIDs", nativeQuery = true)
    fun deleteUsersData(@Param("userIDs") userIDs: List<String>)

}
package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.infrastructure.login.dto.SignupHistoryCountDTO
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

    @Query(
        value = """SELECT DISTINCT DATE(event_date) as date, count(*) as signupCount
            FROM users_data
            WHERE ip_address_hash = :ipAddressHash
            AND user_agent = :userAgent
            AND event_type = 'signup'
            GROUP BY ip_address_hash, DATE(event_date)""",
        nativeQuery = true,
    )
    fun getIpHashSignupHistory(@Param("ipAddressHash") ipAddressHash: String, @Param("userAgent") userAgent: String): List<SignupHistoryCountDTO>

}
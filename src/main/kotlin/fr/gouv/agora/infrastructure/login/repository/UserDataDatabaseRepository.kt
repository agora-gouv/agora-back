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

    @Modifying
    @Transactional
    @Query(value = """WITH 
        suspiciousIpAndUserAgent AS (
            SELECT ip_address_hash, user_agent, count(*) AS signupCount FROM users_data 
            WHERE event_type = 'signup' 
            AND ip_address_hash != ''
            AND event_date > :startDate
            AND event_date < :endDate
            GROUP BY ip_address_hash, user_agent, DATE(event_date)
            HAVING count(*) >= :softBanSignupCount
        ),
        suspiciousUserId AS (
            SELECT DISTINCT user_id FROM users_data
            WHERE ip_address_hash IN (SELECT ip_address_hash FROM suspiciousIpAndUserAgent)
            AND user_agent IN (SELECT user_agent FROM suspiciousIpAndUserAgent)
            AND event_type = 'signup'
        )
        UPDATE agora_users 
        SET is_banned = 1 
        WHERE CAST(id AS TEXT) IN (SELECT user_id FROM suspiciousUserId)
        AND is_banned = 0
    """)
    fun flagUsersWithSuspiciousActivity(
        @Param("softBanSignupCount") softBanSignupCount: Int,
        @Param("startDate") startDate: Date,
        @Param("endDate") endDate: Date,
    ): Int

}
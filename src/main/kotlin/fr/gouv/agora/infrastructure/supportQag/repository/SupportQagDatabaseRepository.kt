package fr.gouv.agora.infrastructure.supportQag.repository

import fr.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Repository
interface SupportQagDatabaseRepository : JpaRepository<SupportQagDTO, UUID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM supports_qag WHERE user_id = :userId AND qag_id = :qagId", nativeQuery = true)
    fun deleteSupportQag(@Param("userId") userId: UUID, @Param("qagId") qagId: UUID): Int

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM supports_qag WHERE qag_id = :qagId", nativeQuery = true)
    fun deleteSupportListByQagId(@Param("qagId") qagId: UUID): Int

    @Query(
        value = """SELECT qag_id FROM supports_qag 
            WHERE qag_id IN (
                SELECT id FROM qags 
                WHERE status = 1 
                OR (status = 0 AND user_id = :userId)
            )
            AND user_id = :userId 
            ORDER BY support_date DESC""",
        nativeQuery = true
    )
    fun getUserSupportedQags(@Param("userId") userId: UUID): List<UUID>

    @Query(value = "SELECT * FROM supports_qag WHERE user_id = :userId AND qag_id = :qagId LIMIT 1", nativeQuery = true)
    fun getSupportQag(@Param("userId") userId: UUID, @Param("qagId") qagId: UUID): SupportQagDTO?

    @Query(
        value = """SELECT count(*) FROM supports_qag 
            WHERE qag_id IN (
                SELECT id FROM qags 
                WHERE status = 1 
                OR (status = 0 AND user_id = :userId)
                AND thematique_id = :thematiqueId
            )
            AND user_id = :userId """,
        nativeQuery = true
    )
    fun getSupportedQagCountByThematique(@Param("userId") userId: UUID, @Param("thematiqueId") thematiqueId: String): Int

    @Query(
        value = """SELECT count(*) FROM supports_qag 
            WHERE qag_id IN (
                SELECT id FROM qags 
                WHERE status = 1 
                OR (status = 0 AND user_id = :userId)
            )
            AND user_id = :userId """,
        nativeQuery = true
    )
    fun getSupportedQagCount(@Param("userId") userId: UUID): Int

    @Modifying
    @Transactional
    @Query(
        value = """DELETE FROM supports_qag
        WHERE user_id IN :userIDs
        AND qag_id IN (
            SELECT id FROM qags
            WHERE status <> 7
        )""", nativeQuery = true
    )
    fun deleteUserSupportsQags(@Param("userIDs") userIDs: List<UUID>)

    @Modifying
    @Transactional
    @Query(
        value = """UPDATE supports_qag
        SET user_id = '00000000-0000-0000-0000-000000000000'
        WHERE user_id IN :userIDs
        AND qag_id IN (
            SELECT id FROM qags
            WHERE status = 7
        )""", nativeQuery = true
    )
    fun anonymizeSupportsOnSelectedQags(@Param("userIDs") userIDs: List<UUID>)

    @Modifying
    @Transactional
    @Query(
        value = """DELETE FROM supports_qag
        WHERE qag_id IN (
            SELECT id FROM qags
            WHERE status <> 7
            AND user_id IN :userIDs
        )""", nativeQuery = true
    )
    fun deleteSupportsOnDeletedUsersQags(@Param("userIDs") userIDs: List<UUID>)

    @Modifying
    @Transactional
    @Query(
        value = """
            DELETE FROM supports_qag
                WHERE qag_id NOT IN (SELECT id FROM qags WHERE status = 7)
                AND user_id IN (SELECT id FROM agora_users WHERE is_banned = 1)
                AND :fromDate < supports_qag.support_date 
        """, nativeQuery = true
    )
    fun deleteBannedUsersSupportsOnUnselectedQags(@Param("fromDate") fromDate: LocalDate): Int
}

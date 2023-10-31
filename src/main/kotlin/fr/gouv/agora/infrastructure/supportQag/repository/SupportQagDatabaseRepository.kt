package fr.gouv.agora.infrastructure.supportQag.repository

import fr.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
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
        value = "SELECT qag_id FROM supports_qag WHERE user_id = :userId ORDER BY support_date DESC",
        nativeQuery = true
    )
    fun getUserSupportedQags(@Param("userId") userId: UUID): List<UUID>

    @Query(value = "SELECT * FROM supports_qag WHERE user_id = :userId AND qag_id = :qagId LIMIT 1", nativeQuery = true)
    fun getSupportQag(@Param("userId") userId: UUID, @Param("qagId") qagId: UUID): SupportQagDTO?
}
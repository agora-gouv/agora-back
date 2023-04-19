package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SupportQagDatabaseRepository : CrudRepository<SupportQagDTO, UUID> {

    @Query(value = "SELECT count(*) FROM supports_qag WHERE qag_id = :qagId", nativeQuery = true)
    fun getSupportCount(@Param("qagId") qagId: UUID): Int

    @Query(value = "SELECT * FROM supports_qag WHERE qag_id = :qagId AND user_id = :userId LIMIT 1", nativeQuery = true)
    fun getSupportQag(@Param("qagId") qagId: UUID, @Param("userId") userId: String): SupportQagDTO?

}
package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.infrastructure.profile.dto.DemographicInfoAskDateDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface DemographicInfoAskDateDatabaseRepository : JpaRepository<DemographicInfoAskDateDTO, UUID> {

    @Query(value = "SELECT * FROM demographic_info_ask_date WHERE user_id = :userId LIMIT 1", nativeQuery = true)
    fun getAskDate(@Param("userId") userId: UUID): DemographicInfoAskDateDTO?

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM demographic_info_ask_date WHERE user_id = :userId", nativeQuery = true)
    fun deleteAskDate(@Param("userId") userId: UUID): Int

}
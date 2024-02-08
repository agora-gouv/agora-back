package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.infrastructure.notification.dto.NotificationDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface NotificationDatabaseRepository : JpaRepository<NotificationDTO, UUID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notifications WHERE user_id IN :userIDs", nativeQuery = true)
    fun deleteUsersNotifications(@Param("userIDs") userIDs: List<UUID>)

}

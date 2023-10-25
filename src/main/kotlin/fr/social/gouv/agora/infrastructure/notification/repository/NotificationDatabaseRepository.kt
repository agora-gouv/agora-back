package fr.social.gouv.agora.infrastructure.notification.repository

import fr.social.gouv.agora.infrastructure.notification.dto.NotificationDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NotificationDatabaseRepository : JpaRepository<NotificationDTO, UUID>

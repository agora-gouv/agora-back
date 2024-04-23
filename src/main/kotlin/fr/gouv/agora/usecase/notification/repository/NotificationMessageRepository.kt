package fr.gouv.agora.usecase.notification.repository

import fr.gouv.agora.infrastructure.notification.dto.NotificationMessageDTO

interface NotificationMessageRepository {

    fun getQagRejected(): NotificationMessageDTO

    fun findAllByStatusAccepted(): Set<NotificationMessageDTO>

    fun getQagAcceptedAfterReject(): NotificationMessageDTO
}


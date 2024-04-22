package fr.gouv.agora.usecase.notification.repository

import fr.gouv.agora.infrastructure.notificationmessage.dto.NotificationMessageDTO

interface NotificationMessageRepository {

    fun getQagRejected(): NotificationMessageDTO

    fun getQagAccepted(): NotificationMessageDTO

    fun getQagAcceptedAfterReject(): NotificationMessageDTO
}


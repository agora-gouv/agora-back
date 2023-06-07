package fr.social.gouv.agora.usecase.notification.repository

interface NotificationMessageRepository {
    fun getQagModeratedNotificationMessage(): String
}


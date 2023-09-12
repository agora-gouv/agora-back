package fr.social.gouv.agora.usecase.notification.repository

interface NotificationMessageRepository {
    fun getQagRejectedTitle(): String
    fun getQagRejectedMessage(): String
    fun getQagAcceptedTitle(): String
    fun getQagAcceptedMessage(): String
    fun getQagAcceptedAfterRejectTitle(): String
    fun getQagAcceptedAfterRejectMessage(): String
}


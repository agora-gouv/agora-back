package fr.social.gouv.agora.infrastructure.notification.repository

import fr.social.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import org.springframework.stereotype.Component

@Component
class NotificationMessageRepositoryImpl : NotificationMessageRepository {

    companion object {
        private const val QAG_MODERATED_NOTIFICATION_MESSAGE =
            "Malheureusement, votre question a du être modérée."
        private const val QAG_MODERATED_NOTIFICATION_TITLE =
            "Question hors Charte"
    }

    override fun getQagModeratedNotificationMessage(): String {
        return QAG_MODERATED_NOTIFICATION_MESSAGE
    }
    override fun getQagModeratedNotificationTitle(): String {
        return QAG_MODERATED_NOTIFICATION_TITLE
    }
}

package fr.social.gouv.agora.infrastructure.notification.repository

import fr.social.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import org.springframework.stereotype.Component

@Component
class NotificationMessageRepositoryImpl : NotificationMessageRepository {

    companion object {
        private const val QAG_MODERATED_NOTIFICATION_MESSAGE =
            "Votre question au Gouvernement a été modérée. Elle ne correspondait malheureusement pas à la charte de participation. Si vous souhaitez en savoir plus, écrivez à "
    }

    override fun getQagModeratedNotificationMessage(): String {
        return QAG_MODERATED_NOTIFICATION_MESSAGE + System.getenv("CONTACT_EMAIL")
    }
}

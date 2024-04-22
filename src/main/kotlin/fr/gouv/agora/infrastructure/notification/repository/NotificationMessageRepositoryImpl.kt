package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.infrastructure.notificationmessage.dto.NotificationMessageDTO
import fr.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class NotificationMessageRepositoryImpl : NotificationMessageRepository {

    companion object {
        private const val QAG_REJECTED_TITLE = "Question hors Charte."
        private const val QAG_REJECTED_MESSAGE = "Malheureusement, votre question a du être modérée."

        private const val QAG_ACCEPTED_TITLE = "Et si vous partagiez votre question ?"
        private const val QAG_ACCEPTED_MESSAGE =
            "Proposez à vos amis de la soutenir pour avoir plus de chance d’obtenir une réponse !"

        private const val QAG_ACCEPTED_AFTER_REJECT_TITLE = "Bonne nouvelle !"
        private const val QAG_ACCEPTED_AFTER_REJECT_MESSAGE =
            "Votre question a été ré-évaluée et correspond à la charte."
    }

    override fun getQagRejected(): NotificationMessageDTO {
        return NotificationMessageDTO(title = QAG_REJECTED_TITLE, message = QAG_REJECTED_MESSAGE)
    }

    override fun getQagAccepted(): NotificationMessageDTO {
        return NotificationMessageDTO(title = QAG_ACCEPTED_TITLE, message = QAG_ACCEPTED_MESSAGE)
    }

    override fun getQagAcceptedAfterReject(): NotificationMessageDTO {
        return NotificationMessageDTO(title = QAG_ACCEPTED_AFTER_REJECT_TITLE, message = QAG_ACCEPTED_AFTER_REJECT_MESSAGE)
    }
}

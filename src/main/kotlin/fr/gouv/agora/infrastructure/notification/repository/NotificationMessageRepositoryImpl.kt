package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.infrastructure.notification.dto.NotificationMessageDTO
import fr.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import org.springframework.stereotype.Component

@Component
class NotificationMessageRepositoryImpl : NotificationMessageRepository {

    companion object {
        private const val QAG_REJECTED_TITLE = "Question hors Charte."
        private const val QAG_REJECTED_MESSAGE = "Malheureusement, votre question a du être modérée."

        private val QAG_ACCEPTED_NOTIFICATIONS = setOf(
            NotificationMessageDTO(
                title = "Et si vous partagiez votre question ?",
                message = "Proposez à vos amis de la soutenir pour avoir plus de chance d’obtenir une réponse !"
            ),
            NotificationMessageDTO(
                title = "✅ Question publiée",
                message = "Votre question est désormais proposée à tous les utilisateurs sur Agora."
            ),
            NotificationMessageDTO(
                title = "🙏 Merci pour votre question !",
                message = "Partagez-la à vos amis pour la faire monter dans le classement."
            ),
            NotificationMessageDTO(
                title = "👋 Votre question est en ligne",
                message = "Faites-la connaître pour obtenir une réponse."
            ),
            NotificationMessageDTO(
                title = "🗓 Votre question sur Agora",
                message = "Elle est publiée. Vous avez jusqu’à lundi pour la faire gagner !"
            ),
            NotificationMessageDTO(
                title = "🥳 Votre question est en ligne",
                message = "Multipliez vos chances de la faire gagner en la partageant."
            ),
            NotificationMessageDTO(
                title = "✅ Question publiée !",
                message = "La réponse pourrait intéresser vos proches ? Partagez-leur pour augmenter vos chances."
            ),
            NotificationMessageDTO(
                title = "🥁 Question publiée avec succès",
                message = "Sera-t-elle en tête lundi à 10h ? Proposez à vos amis de la soutenir."
            ),
        )

        private const val QAG_ACCEPTED_AFTER_REJECT_TITLE = "Bonne nouvelle !"
        private const val QAG_ACCEPTED_AFTER_REJECT_MESSAGE =
            "Votre question a été ré-évaluée et correspond à la charte."
    }

    override fun getQagRejected(): NotificationMessageDTO {
        return NotificationMessageDTO(title = QAG_REJECTED_TITLE, message = QAG_REJECTED_MESSAGE)
    }

    override fun findAllByStatusAccepted(): Set<NotificationMessageDTO> {
        return QAG_ACCEPTED_NOTIFICATIONS
    }

    override fun getQagAcceptedAfterReject(): NotificationMessageDTO {
        return NotificationMessageDTO(
            title = QAG_ACCEPTED_AFTER_REJECT_TITLE,
            message = QAG_ACCEPTED_AFTER_REJECT_MESSAGE
        )
    }
}

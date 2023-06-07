package fr.social.gouv.agora.infrastructure.errorMessages.repository

import fr.social.gouv.agora.usecase.errorMessages.repository.ErrorMessagesRepository
import org.springframework.stereotype.Component

@Component
class ErrorMessagesRepositoryImpl : ErrorMessagesRepository {
    companion object {
        private const val ERROR_TEXT_WITHIN_THE_WEEK =
            "Vous avez déjà posé une question au Gouvernement cette semaine. " +
                    "Pendant cette phase d’expérimentation, l’appli est limitée à une question par semaine pour chaque utilisateur. " +
                    "Nous augmenterons le nombre de questions possibles dès que nos capacités de modération le permettront. " +
                    "Rendez-vous la semaine prochaine pour votre question suivante. D’ici là, n’hésitez pas à soutenir les questions des utilisateurs, sans limite !"

        private const val QAG_MODERATED_NOTIFICATION_MESSAGE =
            "Votre question au Gouvernement a été modérée. Elle ne correspondait malheureusement pas à la charte de participation. Si vous souhaitez en savoir plus, écrivez à "
    }

    override fun getQagDisabledErrorMessage(): String {
        return System.getenv("ERROR_TEXT_QAG_DISABLED")
    }

    override fun getQagErrorMessageOneByWeek(): String {
        return ERROR_TEXT_WITHIN_THE_WEEK
    }

    override fun getQagModeratedNotificationMessage(): String {
        return QAG_MODERATED_NOTIFICATION_MESSAGE + System.getenv("CONTACT_EMAIL")
    }
}
package fr.gouv.agora.infrastructure.errorMessages.repository

import fr.gouv.agora.usecase.errorMessages.repository.ErrorMessagesRepository
import org.springframework.stereotype.Component

@Component
class ErrorMessagesRepositoryImpl : ErrorMessagesRepository {
    companion object {
        private const val ERROR_TEXT_WITHIN_THE_WEEK =
            "Vous avez déjà posé une question au Gouvernement cette semaine. L’appli propose actuellement une question par semaine pour chaque utilisateur afin que le plus grand nombre de citoyens puisse participer. Rendez-vous lundi à partir de 14h pour poser une nouvelle question. D’ici là, n’hésitez pas à soutenir les questions des autres utilisateurs, sans limite !"
    }

    override fun getQagDisabledErrorMessage(): String {
        return System.getenv("ERROR_TEXT_QAG_DISABLED")
    }

    override fun getQagErrorMessageOneByWeek(): String {
        return ERROR_TEXT_WITHIN_THE_WEEK
    }
}

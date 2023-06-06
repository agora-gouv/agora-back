package fr.social.gouv.agora.infrastructure.errorMessages.repository

import fr.social.gouv.agora.usecase.errorMessages.repository.ErrorMessagesRepository
import org.springframework.stereotype.Component

@Component
class ErrorMessagesRepositoryImpl : ErrorMessagesRepository {

    override fun getQagErrorMessageFromSystemEnv(): String {
        return System.getenv("ERROR_TEXT_QAG_DISABLED")
    }
}
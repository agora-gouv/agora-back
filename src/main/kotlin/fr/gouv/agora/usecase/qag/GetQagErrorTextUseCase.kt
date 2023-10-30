package fr.gouv.agora.usecase.qag

import fr.gouv.agora.usecase.errorMessages.repository.ErrorMessagesRepository
import org.springframework.stereotype.Service

@Service
class GetQagErrorTextUseCase(
    private val errorMessagesRepository: ErrorMessagesRepository,
    private val getAskQagStatusUseCase: GetAskQagStatusUseCase,
) {
    fun getGetQagErrorText(userId: String): String? {
        return when (getAskQagStatusUseCase.getAskQagStatus(userId = userId)) {
            AskQagStatus.ENABLED -> null
            AskQagStatus.FEATURE_DISABLED -> errorMessagesRepository.getQagDisabledErrorMessage()
            AskQagStatus.WEEKLY_LIMIT_REACHED -> errorMessagesRepository.getQagErrorMessageOneByWeek()
        }
    }
}

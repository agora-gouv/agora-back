package fr.gouv.agora.usecase.appFeedback

import fr.gouv.agora.domain.AppFeedbackInserting
import fr.gouv.agora.usecase.appFeedback.repository.AppFeedbackRepository
import org.springframework.stereotype.Component

@Component
class InsertAppFeedbackUseCase(
    private val repository: AppFeedbackRepository,
) {

    fun insertAppFeedback(appFeedback: AppFeedbackInserting): InsertAppFeedbackResult {
        return when (repository.insertAppFeedback(appFeedback)) {
            true -> InsertAppFeedbackResult.SUCCESS
            false -> InsertAppFeedbackResult.FAILURE
        }
    }

}

enum class InsertAppFeedbackResult {
    SUCCESS, FAILURE
}
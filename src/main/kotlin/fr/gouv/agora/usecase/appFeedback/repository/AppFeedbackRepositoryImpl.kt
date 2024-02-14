package fr.gouv.agora.usecase.appFeedback.repository

import fr.gouv.agora.domain.AppFeedbackInserting
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class AppFeedbackRepositoryImpl(
    private val databaseRepository: AppFeedbackDatabaseRepository,
    private val mapper: AppFeedbackMapper,
) : AppFeedbackRepository {

    override fun insertAppFeedback(appFeedback: AppFeedbackInserting): Boolean {
        return mapper.toDto(appFeedback)?.let { appFeedbackDTO ->
            databaseRepository.save(appFeedbackDTO)
            true
        } ?: false
    }

}
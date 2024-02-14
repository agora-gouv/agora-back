package fr.gouv.agora.usecase.appFeedback.repository

import fr.gouv.agora.domain.AppFeedbackInserting

interface AppFeedbackRepository {
    fun insertAppFeedback(appFeedback: AppFeedbackInserting): Boolean
}
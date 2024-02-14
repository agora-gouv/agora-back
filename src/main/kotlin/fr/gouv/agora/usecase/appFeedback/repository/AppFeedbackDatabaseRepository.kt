package fr.gouv.agora.usecase.appFeedback.repository

import fr.gouv.agora.infrastructure.appFeedback.dto.AppFeedbackDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AppFeedbackDatabaseRepository : JpaRepository<AppFeedbackDTO, UUID> {}
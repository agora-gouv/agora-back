package fr.gouv.agora.infrastructure.login.dto

import org.springframework.data.rest.core.config.Projection
import java.time.LocalDate

@Projection(types = [SignupHistoryCountDTO::class])
interface SignupHistoryCountDTO {
    val date: LocalDate
    val signupCount: Int
}

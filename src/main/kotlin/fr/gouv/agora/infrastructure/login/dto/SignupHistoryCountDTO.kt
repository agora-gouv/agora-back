package fr.gouv.agora.infrastructure.login.dto

import java.time.LocalDate

interface SignupHistoryCountDTO {
    val date: LocalDate
    val signupCount: Int
}

package fr.gouv.agora.domain

import java.time.LocalDate

data class SignupHistoryCount(
    val date: LocalDate,
    val signupCount: Int,
)

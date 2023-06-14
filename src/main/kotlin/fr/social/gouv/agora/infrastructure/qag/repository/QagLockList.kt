package fr.social.gouv.agora.infrastructure.qag.repository

import java.time.LocalDate

data class QagLockList(
    val userId: String,
    val qagIdList: List<String>,
    val dateLockedAt: Long,
)

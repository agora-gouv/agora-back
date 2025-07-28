package fr.gouv.agora.domain

import java.time.LocalDateTime

data class News(
    val description: String,
    val short_description: String,
    val callToActionText: String,
    val routeName: String,
    val routeArgument: String?,
    val beginDate: LocalDateTime,
)

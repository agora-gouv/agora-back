package fr.gouv.agora.infrastructure.welcomePage

data class NewsJson(
    val description: String,
    val callToActionText: String,
    val routeName: String,
    val routeArgument: String?,
)

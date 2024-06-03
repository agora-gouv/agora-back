package fr.gouv.agora.infrastructure.welcomePage

data class LastNewsJson(
    val description: String,
    val callToActionText: String,
    val routeName: String,
    val routeArgument: String,
)

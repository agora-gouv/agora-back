package fr.gouv.agora.domain

data class Explanation(
    val isTogglable: Boolean,
    val title: String,
    val intro: String,
    val image: ExplanationImage?,
    val description: String,
)

data class ExplanationImage(
    val url: String,
    val description: String?,
)
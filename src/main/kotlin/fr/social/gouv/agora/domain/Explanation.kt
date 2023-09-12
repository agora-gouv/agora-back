package fr.social.gouv.agora.domain

data class Explanation(
    val isTogglable: Boolean,
    val title: String,
    val intro: String,
    val imageUrl: String,
    val description: String,
)
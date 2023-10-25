package fr.social.gouv.agora.domain

data class SupportQagInserting(
    val qagId: String,
    val userId: String,
)

data class SupportQagDeleting(
    val qagId: String,
    val userId: String,
)
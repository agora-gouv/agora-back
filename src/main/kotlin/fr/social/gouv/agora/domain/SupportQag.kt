package fr.social.gouv.agora.domain

data class SupportQag(
    val supportCount: Int,
    val isSupportedByUser: Boolean,
)

data class SupportQagInserting(
    val userId: String,
    val qagId: String,
)
data class SupportQagDeleting(
    val userId: String,
    val qagId: String,
)
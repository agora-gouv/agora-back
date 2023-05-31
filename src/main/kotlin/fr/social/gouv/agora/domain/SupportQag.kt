package fr.social.gouv.agora.domain

import java.util.Date

data class SupportQagInfo(
    val qagId: String,
    val userId: String,
    val supportDate: Date,
)

data class SupportQag(
    val supportCount: Int,
    val isSupportedByUser: Boolean,
)

data class SupportQagInserting(
    val qagId: String,
    val userId: String,
)

data class SupportQagDeleting(
    val qagId: String,
    val userId: String,
)
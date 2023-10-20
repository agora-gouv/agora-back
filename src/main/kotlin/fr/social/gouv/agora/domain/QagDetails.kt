package fr.social.gouv.agora.domain

import java.util.*

data class QagDetails(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val description: String,
    val date: Date,
    val status: QagStatus,
    val username: String,
    val userId: String,
    val supportCount: Int,
    val response: ResponseQag?,
    val feedbackResults: FeedbackResults?,
)

data class QagWithUserData(
    val qagDetails: QagDetails,
    val canShare: Boolean,
    val canSupport: Boolean,
    val canDelete: Boolean,
    val isAuthor: Boolean,
    val isSupportedByUser: Boolean,
    val hasGivenFeedback: Boolean,
)
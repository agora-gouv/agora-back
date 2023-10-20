package fr.social.gouv.agora.domain

data class IncomingResponsePreview(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val supportCount: Int,
)
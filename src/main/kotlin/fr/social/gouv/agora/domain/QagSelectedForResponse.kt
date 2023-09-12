package fr.social.gouv.agora.domain

data class QagSelectedForResponse(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val support: SupportQag,
)
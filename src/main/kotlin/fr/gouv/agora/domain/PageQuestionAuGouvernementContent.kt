package fr.gouv.agora.domain

data class PageQuestionAuGouvernementContent(
    val informationBottomsheet: String,
    val texteTotalQuestions: String,
    val programmeDuMois: String? = null,
    val commentCaMarche: String? = null,
)
